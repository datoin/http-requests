package org.datoin.net.http.methods;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.datoin.net.http.Request;
import org.datoin.net.http.Response;

import java.io.File;
import java.io.InputStream;

/**
 * Author : umarshah@simplyphi.com
 * Created on : 7/5/14.
 */
public class Put extends Request {
    public  Put(String url) {
        super(url);
    }

    @Override
    public Response execute() {
        return put();
    }

    public Response putContent(InputStream stream, ContentType contentType) {
        HttpEntity entity = new InputStreamEntity(stream, contentType);
        RequestBuilder req = RequestBuilder.put().setUri(url);
        return getResponse(entity, req);
    }

    public Response putContent(File file) {
        HttpEntity entity = new FileEntity(file);
        RequestBuilder req = RequestBuilder.put().setUri(url);
        return getResponse(entity, req);
    }
}
