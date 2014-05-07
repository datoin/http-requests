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

    public Request setParam(String name, String value) {
        this.params.put(name, value);
        return this;
    }

    // getter setters from here

    public int getConnectionTimeOutMillis() {
        return connectionTimeOutMillis;
    }

    public Request setConnectionTimeOutMillis(int connectionTimeOutMillis) {
        this.connectionTimeOutMillis = connectionTimeOutMillis;
        return this;
    }

    public int getSocketTimeOutMillis() {
        return socketTimeOutMillis;
    }

    public Request setSocketTimeOutMillis(int socketTimeOutMillis) {
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
}
