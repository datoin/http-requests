package org.datoin.net.http.methods;

import org.datoin.net.http.Request;
import org.datoin.net.http.Response;

/**
 * Author : umarshah@simplyphi.com
 * Created on : 7/5/14.
 */
public class Head extends Request {
    public Head(String url) {
        super(url);
    }

    @Override
    public Response execute() {
        return head();
    }
}
