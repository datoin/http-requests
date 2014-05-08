package org.datoin.net.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Author : umarshah@simplyphi.com
 * Created on : 6/5/14.
 */
public class Response {
    private int status;
    private String content;
    private String contentType;
    private long contentLength;
    private String statusLine ;
    private String requestLine ;
    private String method;
    private HashMap<String, Object> responseHeaders = new HashMap<String, Object>();

    public Response() {
    }

    public Response(int status, String content, String contentType, long contentLength) {
        this.status = status;
        this.content = content;
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public String getStatusLine() {
        return statusLine;
    }

    public void setStatusLine(String statusLine) {
        this.statusLine = statusLine;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestLine() {
        return requestLine;
    }

    public void setRequestLine(String requestLine) {
        this.requestLine = requestLine;
    }

    public void setHeaders(Header[] headers){
        for(Header header : headers){
            responseHeaders.put(header.getName(), header.getValue());
        }
    }
    private static String parseEntity(HttpEntity resEntity) {
        String line = "";
        StringBuilder result = new StringBuilder();
        BufferedReader rd = null;
        try {
            rd = new BufferedReader(new InputStreamReader(resEntity.getContent()));
            while ((line = rd.readLine()) != null) {
                result = result.append(line);
            }
        } catch (IOException e) {
            // TODO: log this error
            e.printStackTrace();
        }
        return result.toString();
    }

    private boolean isTextContent(String contentType){
        return ( contentType.contains("text") ||
                contentType.contains("json") ||
                contentType.contains("xml"));
    }
    /**
     * update Response object from given http response
     * @param resp
     */
    public void updateResponse(CloseableHttpResponse resp){
        // TODO: update logging
        setStatus(resp.getStatusLine().getStatusCode());
        setStatusLine(resp.getStatusLine().toString());
        setHeaders(resp.getAllHeaders());
        final HttpEntity entity = resp.getEntity();
        if(entity != null) {
            final Header contentTypeVal = entity.getContentType();
            if(contentTypeVal != null) {
                setContentType(contentTypeVal.getValue());
            }
            setContentLength(entity.getContentLength());
            if (contentLength != 0 && isTextContent(contentType)) {
                setContent(parseEntity(entity));
            }
        }

    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("// Request: ");
        sb.append(requestLine).append("\n// Status: ").append(statusLine).append("\n// Headers:\n");
        for(String key : responseHeaders.keySet()){
            sb.append("// ").append(key).append(" : ").append(responseHeaders.get(key)).append("\n");
        }
        sb.append("\n// Method : ").append(method).append("\n\n\n")
          .append(content);
        return sb.toString();

    }
}
