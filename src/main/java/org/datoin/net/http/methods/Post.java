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
 * Created on : 6/5/14.
 */
public class Post extends Request {
    public Post(String url) {
        super(url);
    }

    @Override
    public Response execute() {
        return post();
    }

    public Response postContent(InputStream stream, ContentType contentType) {
        HttpEntity entity = new InputStreamEntity(stream, contentType);
        RequestBuilder req = RequestBuilder.post().setUri(url);
        return getResponse(entity, req);
    }

    public Response postContent(File file) {
        HttpEntity entity = new FileEntity(file);
        RequestBuilder req = RequestBuilder.post().setUri(url);
        return getResponse(entity, req);
    }

}
