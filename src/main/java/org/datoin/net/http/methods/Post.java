package org.datoin.net.http.methods;

import org.datoin.net.http.Request;
import org.datoin.net.http.Response;

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

}
