package org.datoin.net.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
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

    protected Request(String url) {
        this.url = url;
    }

    public abstract Response execute();


    private CloseableHttpClient getClient(){
        RequestConfig rConf = RequestConfig.custom()
                .setSocketTimeout(socketTimeOutMillis)
                .setConnectTimeout(connectionTimeOutMillis)
                .build();

        CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(rConf).build();
        return client;
    }

    private void initHeaders(RequestBuilder req){
        if (headers != null) {
            for (Map.Entry<String, String> param : headers.entrySet()) {
                req.addHeader(param.getKey(), param.getValue());
            }
        }
    }

    private void initParams(RequestBuilder req){
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                req.addParameter(param.getKey(), param.getValue());
            }
        }
    }

    /**
     * execute a post request for this Request object
     * @return  : Response object built from the http resposne
     */
    protected Response post() {
        CloseableHttpClient client = getClient();
        RequestBuilder postRequest = RequestBuilder.post().setUri(url);
        initHeaders(postRequest);
        Response response = new Response();
        response.setMethod(Methods.POST.getMethod());


        final MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        for (Map.Entry<String, String> param : params.entrySet()) {
            reqEntity.addTextBody(param.getKey(), param.getValue());
        }

        for (String streamName : postStreams.keySet()) {
            reqEntity.addBinaryBody(streamName, postStreams.get(streamName));
        }

        postRequest.setEntity(reqEntity.build());
        CloseableHttpResponse resp = null;
        try {
            final HttpUriRequest uriRequest = postRequest.build();
            resp = client.execute(uriRequest);
            response.updateResponse(resp);
            response.setRequestLine(uriRequest.getRequestLine().toString());
        } catch (Exception e) {
            // TODO: log the error
            e.printStackTrace();
        } finally {
            try {
                resp.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    /**
     * execute a get request from the Request object configuration
     * @return : a formatted Response object
     */
    protected Response get() {
        CloseableHttpClient client = getClient();
        RequestBuilder getRequest = RequestBuilder.get().setUri(url);
        Response response = new Response();
        response.setMethod(Methods.GET.getMethod());
        initHeaders(getRequest);
        initParams(getRequest);

        CloseableHttpResponse resp = null;
        try {
            final HttpUriRequest uriRequest = getRequest.build();
            resp = client.execute(uriRequest);
            // but make sure the response gets closed no matter what
            // even if do not care about its content
            response.updateResponse(resp);
            response.setRequestLine(uriRequest.getRequestLine().toString());

        } catch (Exception e) {
            // TODO: log the error
            e.printStackTrace();
        } finally {
            try {
                resp.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return response;
    }

    protected Response put() {
        CloseableHttpClient client = getClient();
        RequestBuilder getRequest = RequestBuilder.put().setUri(url);
        Response response = new Response();
        response.setMethod(Methods.PUT.getMethod());
        initHeaders(getRequest);
        initParams(getRequest);

        CloseableHttpResponse resp = null;
        try {
            final HttpUriRequest uriRequest = getRequest.build();
            resp = client.execute(uriRequest);
            // but make sure the response gets closed no matter what
            // even if do not care about its content
            response.updateResponse(resp);
            response.setRequestLine(uriRequest.getRequestLine().toString());


        } catch (Exception e) {
            // TODO: log the error
            e.printStackTrace();
        } finally {
            try {
                resp.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return response;
    }

    protected Response head() {
        CloseableHttpClient client = getClient();
        RequestBuilder getRequest = RequestBuilder.head().setUri(url);
        Response response = new Response();
        response.setMethod(Methods.HEAD.getMethod());
        initHeaders(getRequest);
        initParams(getRequest);

        CloseableHttpResponse resp = null;
        try {
            final HttpUriRequest uriRequest = getRequest.build();
            resp = client.execute(uriRequest);
            // but make sure the response gets closed no matter what
            // even if do not care about its content
            response.updateResponse(resp);
            response.setRequestLine(uriRequest.getRequestLine().toString());

        } catch (Exception e) {
            // TODO: log the error
            e.printStackTrace();
        } finally {
            try {
                resp.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

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
     * get specific param value
     * @param paramName    : name of the param to fetch value for
     * @return  : value of corresponding param name
     */
    public String getParam(String paramName) {
        return this.params.get(paramName);
    }
}
