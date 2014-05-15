package org.datoin.net.http;

import org.datoin.net.http.methods.*;

/**
 * Author : umarshah@simplyphi.com
 * Created on : 6/5/14.
 */
public class Requests {
//    public Response execute
    public static Get get(String url){
        return new Get(url);
    }

    public static Post post(String url){
        return new Post(url);
    }

    public static Put put(String url) { return new Put(url);}

    public static Head head(String url) { return new Head(url);}

    public static Delete delete(String url) { return new Delete(url);}
}
