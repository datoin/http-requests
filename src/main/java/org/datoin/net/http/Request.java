package org.datoin.net.http;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.datoin.net.http.methods.Methods;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Author : umarshah@simplyphi.com
 * Created on : 6/5/14.
 */
public abstract class Request {

    protected String url;
    private int connectionTimeOutMillis = 180000;    // default 3 minutes
    private int socketTimeOutMillis = 1800000;    // default 30 minutes
    protected HashMap<String, String> headers = new HashMap<String, String>();
    protected HashMap<String, String> params = new HashMap<String, String>();
    protected HashMap<String, InputStream> postStreams = new HashMap<String, InputStream>();
    private HttpEntity entity = null;

    protected Request(String url) {
        this.url = url;
    }

    public abstract Response execute();


   protected CloseableHttpClient getClient(){
        RequestConfig rConf = RequestConfig.custom()
                .setSocketTimeout(socketTimeOutMillis)
                .setConnectTimeout(connectionTimeOutMillis)
                .build();

        CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(rConf).build();
        return client;
    }

    protected void initHeaders(RequestBuilder req){
        if (headers != null) {
            for (Map.Entry<String, String> param : headers.entrySet()) {
                req.addHeader(param.getKey(), param.getValue());
            }
        }
    }

    protected void initParams(RequestBuilder req){
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                req.addParameter(param.getKey(), param.getValue());
            }
        }
    }

    protected Response executeHttpRequest(RequestBuilder httpRequest){
        CloseableHttpClient client = getClient();
        initHeaders(httpRequest);
        initParams(httpRequest);
        Response response = null;
        CloseableHttpResponse resp = null;
        if( entity !=null ){
            httpRequest.setEntity(entity);
        }
        try {
            final HttpUriRequest uriRequest = httpRequest.build();

            resp = client.execute(uriRequest);
            // but make sure the response gets closed no matter what
            // even if do not care about its content
            response = new Response(resp);
            response.setMethod(httpRequest.getMethod());
            response.setRequestLine(uriRequest.getRequestLine().toString());
        } catch (Exception e) {
            // TODO: log the error
            e.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(client);
        }
        return response;
    }

    /**
     * execute a get request from the Request object configuration
     * @return : a formatted Response object
     */
    protected Response get() {
        RequestBuilder getRequest = RequestBuilder.get().setUri(url);
        return executeHttpRequest(getRequest);
    }

    protected Response head() {
        RequestBuilder putRequest = RequestBuilder.head().setUri(url);
        return executeHttpRequest(putRequest);
    }

    protected Response delete() {
        RequestBuilder builder = RequestBuilder.delete().setUri(url);
        return getResponseAfterDetectingType(builder);
    }

    /**
     * execute a post request for this Request object
     * @return  : Response object built from the http resposne
     */
    protected Response post() {
        RequestBuilder builder = RequestBuilder.post().setUri(url);
        return getResponseAfterDetectingType(builder);
    }

    protected Response put() {
        RequestBuilder builder = RequestBuilder.put().setUri(url);
        return getResponseAfterDetectingType(builder);

    }

    private Response getResponseAfterDetectingType(RequestBuilder builder) {
        // check whether post is a multipart body request
        if(entity != null || postStreams.size() ==0){
            return executeHttpRequest(builder);
        }

        return getResponseFromMultiPartRequest(builder);
    }

    private Response getResponseFromMultiPartRequest(RequestBuilder request) {
        CloseableHttpClient client = getClient();
        initHeaders(request);
        Response response = null;

        final MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        for (Map.Entry<String, String> param : params.entrySet()) {
            reqEntity.addTextBody(param.getKey(), param.getValue());
        }

        for (String streamName : postStreams.keySet()) {
            reqEntity.addBinaryBody(streamName, postStreams.get(streamName));
        }

        request.setEntity(reqEntity.build());
        CloseableHttpResponse resp = null;
        try {
            final HttpUriRequest uriRequest = request.build();
            resp = client.execute(uriRequest);
            response = new Response(resp);
            response.setMethod(Methods.POST.getMethod());
            response.setRequestLine(uriRequest.getRequestLine().toString());
        } catch (Exception e) {
            // TODO: log the error
            e.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(client);
        }
        return response;
    }

    /**
     * seat a header and return modified request
     *
     * @param name  : header name
     * @param value : header value
     * @return
     */
    public Request setHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    /**
     * sets the accept header for the request, if exists then appends
     * @param headerValue   : header field value to add
     * @return
     */
    public Request accept(String headerValue){
        final String accept = RequestHeaderFields.ACCEPT.getName();
        String acceptValue = headers.get(accept);
        if (acceptValue == null){
            acceptValue = headerValue;
        } else {
            acceptValue = acceptValue + ", " + headerValue;
        }
        return this.setHeader(accept, acceptValue);
    }

    /**
     * sets the accept encoding header for the request, if exists then appends
     * @param encoding   : header field value to add
     * @return
     */
    public Request acceptEncoding(String encoding){
        final String acceptEncoding = RequestHeaderFields.ACCEPT_ENCODING.getName();
        String encodingValue = headers.get(acceptEncoding);
        if (encodingValue == null){
            encodingValue = encoding;
        } else {
            encodingValue = encodingValue + ", " + encoding;
        }
        return this.setHeader(acceptEncoding, encodingValue);
    }

    /**
     * sets the accept language header for the request, if exists then appends
     * @param language   : header field value to add
     * @return
     */
    public Request acceptLanguage(String language){
        final String acceptLanguage = RequestHeaderFields.ACCEPT_LANGUAGE.getName();
        String languageValue = headers.get(acceptLanguage);
        if (languageValue == null){
            languageValue = language;
        } else {
            languageValue = languageValue + ", " + language;
        }
        return this.setHeader(acceptLanguage, languageValue);
    }

    /**
     * sets the user agent header for the request, if exists then appends
     * @param userAgentValue   : header field value to add
     * @return
     */
    public Request userAgent(String userAgentValue){
        final String userAgent = RequestHeaderFields.USER_AGENT.getName();
        String agent = headers.get(userAgent);
        if (agent == null){
            agent = userAgentValue;
        } else {
            agent = agent + " " + userAgentValue;
        }
        return this.setHeader(userAgent, agent);
    }

    /**
     * sets the Referer header for the request
     * @param refererValue   : header field value to add
     * @return
     */
    public Request referer(String refererValue){
        final String referer = RequestHeaderFields.REFERER.getName();
        return this.setHeader(referer, refererValue);
    }

    /**
     * sets the Authorization  header for the request
     * @param authorizationValue   : header field value to add
     * @return
     */
    public Request authorization(String authorizationValue){
        final String authorization = RequestHeaderFields.AUTHORIZATION.getName();
        return this.setHeader(authorization, authorizationValue);
    }

    public Request authorize(String name, String password){
        String authString = name + ":" + password;
        System.out.println("auth string: " + authString);
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);
        System.out.println("Base64 encoded auth string: " + authStringEnc);
        return this.authorization("Basic " + authStringEnc);

    }

    /**
     * sets the if modified since header for the request (use with GET only)
     * @param dateSince   : header field value to add
     * @return
     */
    public Request ifModifiedSince(String dateSince){
        final String ifmodsin = RequestHeaderFields.IF_MODIFIED_SINCE.getName();
        return this.setHeader(ifmodsin, dateSince);
    }

    /**
     * sets the Pragma no-cache header for the request
     * @return
     */
    public Request pragmaNoCache(){
        final String pragma = RequestHeaderFields.PRAGMA.getName();
        return this.setHeader(pragma, "no-cache");
    }

    /**
     * set a request param and return modified request
     * @param name  : name of teh request param
     * @param value : value of teh request param
     * @return
     */
    public Request setParam(String name, String value) {
        this.params.put(name, value);
        return this;
    }

    // getter setters from here

    public int getConnectionTimeOutMillis() {
        return connectionTimeOutMillis;
    }

    /**
     * set connection timeout
     * @param connectionTimeOutMillis   : milliseconds to timeout connection
     * @return
     */
    public Request connectionTimeOut(int connectionTimeOutMillis) {
        this.connectionTimeOutMillis = connectionTimeOutMillis;
        return this;
    }

    public int getSocketTimeOutMillis() {
        return socketTimeOutMillis;
    }

    /**
     * set socket time out
     * @param socketTimeOutMillis   : milliseconds to timeout socket transaction
     * @return
     */
    public Request socketTimeOut(int socketTimeOutMillis) {
        this.socketTimeOutMillis = socketTimeOutMillis;
        return this;
    }

    // for post only
    public Request addInputStream(String name , File file) throws FileNotFoundException {
        this.postStreams.put(name, new FileInputStream(file));
        return this;
    }

    public Request addInputStream(String name , InputStream inputStream) {
        this.postStreams.put(name, inputStream);
        return this;
    }

    public String getUrl() {
        return url;
    }

    /**
     * get specific header value
     * @param headerName    : name of teh header field to fetch value for
     * @return  : value of corresponding header field
     */
    public String getHeader(String headerName) {
        return this.headers.get(headerName);
    }

    /**
     * set request content from input text string
     * @param text
     * @return modified Request object
     * @throws UnsupportedEncodingException
     */
    public Request setContent(String text) throws UnsupportedEncodingException {
        entity = new StringEntity(text);
        return this;
    }

    /**
     * set request content from input text string with given content type
     * @param text
     * @return modified Request object
     * @throws UnsupportedEncodingException
     */
    public Request setContent(String text, ContentType contentType) {
        entity = new StringEntity(text, contentType);
        return this;
    }

    /**
     * set request content from input stream with given content type
     * @param stream
     * @return modified Request object
     * @throws UnsupportedEncodingException
     */
    public Request setContent(InputStream stream, ContentType contentType) {
        entity = new InputStreamEntity(stream, contentType);
        return this;
    }

    /**
     * set request content from input File
     * @param file
     * @return modified Request object
     * @throws UnsupportedEncodingException
     */
    public Request setContent(File file) {
        entity = new FileEntity(file);
        return this;
    }

    /**
     * get specific param value
     * @param paramName    : name of the param to fetch value for
     * @return  : value of corresponding param name
     */
    public String getParam(String paramName) {
        return this.params.get(paramName);
    }

    protected Response getResponse(HttpEntity entity, RequestBuilder req) {
        CloseableHttpClient client = getClient();
        initHeaders(req);
        req.setEntity(entity);
        CloseableHttpResponse resp = null;
        Response response = null;
        try {
            final HttpUriRequest uriRequest = req.build();
            resp = client.execute(uriRequest);
            response = new Response(resp);
            response.setMethod(Methods.POST.getMethod());
            response.setRequestLine(uriRequest.getRequestLine().toString());
        } catch (Exception e) {
            // TODO: log the error
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}
