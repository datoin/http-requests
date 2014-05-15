package org.datoin.net.http.methods;

/**
 * Author : umarshah@simplyphi.com
 * Created on : 7/5/14.
 */
public enum Methods {
    GET ("GET"),
    POST ("POST"),
    PUT ("PUT"),
    HEAD ("HEAD"),
    DELETE ("DELETE");


    private String method;

    Methods(String method) {

        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
