package org.datoin.net.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Author : umarshah@simplyphi.com
 * Created on : 6/5/14.
 */
public class Response {
    private int status;
    private String content;
    private ContentType contentType;
    private long contentLength;
    private String statusLine;
    private String requestLine;
    private String method;
    private CloseableHttpResponse httpResp;
    private byte[] contentBytes = null;

    private HashMap<String, Object> responseHeaders = new HashMap<String, Object>();

    public Response() {
    }

    public Response(CloseableHttpResponse httpResp) {
        this.httpResp = httpResp;
        updateResponse();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
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

    public void setHeaders(Header[] headers) {
        for (Header header : headers) {
            responseHeaders.put(header.getName(), header.getValue());
        }
    }

    /**
     * update Response object from given http response
     */
    public void updateResponse() {
        // TODO: update logging
        setStatus(httpResp.getStatusLine().getStatusCode());
        setStatusLine(httpResp.getStatusLine().toString());
        setHeaders(httpResp.getAllHeaders());
        final HttpEntity entity = httpResp.getEntity();
        if (entity != null) {
            try {
                contentBytes = EntityUtils.toByteArray(entity);
            } catch (IOException e) {
                contentBytes = new byte[0]; // empty byte array
                e.printStackTrace();
            }
            ContentType contentType = ContentType.getOrDefault(entity);
            setContentType(contentType);
            setContentLength(entity.getContentLength());
        }
        System.out.println(this);

    }

    public String getContentAsString() {
        if (content == null) {
            HttpEntity entity = httpResp.getEntity();

            if (contentBytes.length > 0 && contentLength != 0) {
                Charset charset = contentType.getCharset();
                if (charset != null) {
                    content = new String(contentBytes, charset);
                } else {
                    content = new String(contentBytes);
                }
            } else {
                content = "";
            }
        }
        return content;
    }

    public byte[] getContentAsByteArray() {
        return contentBytes;
    }

    public CloseableHttpResponse getHttpResp() {
        return httpResp;
    }

    public void setHttpResp(CloseableHttpResponse httpResp) {
        this.httpResp = httpResp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("// Request: ");
        sb.append(requestLine).append("\n// Status: ").append(statusLine).append("\n// Headers:\n");
        for (String key : responseHeaders.keySet()) {
            sb.append("// ").append(key).append(" : ").append(responseHeaders.get(key)).append("\n");
        }
        sb.append("\n// Method : ").append(method);
        return sb.toString();

    }

    @Override
    protected void finalize() throws Throwable {
        if (httpResp != null) httpResp.close();
        super.finalize();
    }
}
