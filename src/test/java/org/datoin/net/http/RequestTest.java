package org.datoin.net.http;

import junit.framework.Assert;

public class RequestTest {

    @org.junit.Test
    public void testRequest() throws Exception {
        final String url = "test.com";
        final Request req = Requests.get(url);
        Assert.assertEquals("url does not match", url, req.getUrl());
    }

    @org.junit.Test
    public void testSetHeader() throws Exception {
        final Request req = Requests.get("test.com").setHeader("alpha", "beta");
        Assert.assertEquals("Set Header Mismatch", "beta", req.getHeader("alpha"));

    }

    @org.junit.Test
    public void testAccept() throws Exception {
        final Request req = Requests.post("").accept("everything");
        Assert.assertEquals("Accept Header mismatch", "everything",
                req.getHeader(RequestHeaderFields.ACCEPT.getName()));

    }

    @org.junit.Test
    public void testAcceptEncoding() throws Exception {
        final String encoding = "some encoding";
        final Request req = Requests.head("anything").acceptEncoding(encoding);
        Assert.assertEquals("Accept Encoding Header mismatch", encoding,
                req.getHeader(RequestHeaderFields.ACCEPT_ENCODING.getName()));
    }

    @org.junit.Test
    public void testAcceptLanguage() throws Exception {
        final String language = "some language";
        final Request req = Requests.head("anything").acceptLanguage(language);
        Assert.assertEquals("Accept Language Header mismatch", language,
                req.getHeader(RequestHeaderFields.ACCEPT_LANGUAGE.getName()));
    }

    @org.junit.Test
    public void testUserAgent() throws Exception {
        final String agent = "some agent";
        final Request req = Requests.head("anything").userAgent(agent);
        Assert.assertEquals("User Agent Header mismatch", agent,
                req.getHeader(RequestHeaderFields.USER_AGENT.getName()));

    }

    @org.junit.Test
    public void testReferer() throws Exception {
        final String referer = "some referer";
        final Request req = Requests.put("anything").referer(referer);
        Assert.assertEquals("Referer Header mismatch", referer,
                req.getHeader(RequestHeaderFields.REFERER.getName()));

    }

    @org.junit.Test
    public void testAuthorization() throws Exception {
        final String authorization = "some authorization";
        final Request req = Requests.put("anything").authorization(authorization);
        Assert.assertEquals("Authorization Header mismatch", authorization,
                req.getHeader(RequestHeaderFields.AUTHORIZATION.getName()));


    }

    @org.junit.Test
    public void testIfModifiedSince() throws Exception {
        final String ifmodsince = "some ifmodsince";
        final Request req = Requests.put("anything").ifModifiedSince(ifmodsince);
        Assert.assertEquals("If MOdified Since Header mismatch", ifmodsince,
                req.getHeader(RequestHeaderFields.IF_MODIFIED_SINCE.getName()));


    }

    @org.junit.Test
    public void testPragmaNoCache() throws Exception {
        final String pragma = "no-cache";
        final Request req = Requests.get("anything").pragmaNoCache();
        Assert.assertEquals("Pragma Header mismatch", pragma,
                req.getHeader(RequestHeaderFields.PRAGMA.getName()));


    }

    @org.junit.Test
    public void testSetParam() throws Exception {
        final String  paramNme = "some param";
        final String paramValue = "some value";
        final Request req = Requests.head("anything").setParam(paramNme, paramValue);
        Assert.assertEquals("Param mismatch", paramValue,req.getParam(paramNme));

    }

    @org.junit.Test
    public void testGetAndSetConnectionTimeOutMillis() {
        final int connectionTimeOutMillis = 10980;
        final Request req = Requests.post("something").connectionTimeOut(connectionTimeOutMillis);

        Assert.assertEquals("Connection Timeout mismatch", connectionTimeOutMillis,
                req.getConnectionTimeOutMillis());
    }

    @org.junit.Test
    public void testGetAndSetSocketTimeOutMillis()  {
        final int socketTimeOutMillis = 12100;
        final Request req = Requests.post("something").socketTimeOut(socketTimeOutMillis);

        Assert.assertEquals("socket Timeout mismatch", socketTimeOutMillis, req.getSocketTimeOutMillis());
    }

}