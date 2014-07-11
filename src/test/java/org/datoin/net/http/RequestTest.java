package org.datoin.net.http;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class RequestTest {

    @Test
    public void testRequest() throws Exception {
        final String url = "test.com";
        final Request req = Requests.get(url);
        Assert.assertEquals("url does not match", url, req.getUrl());
    }

    @Test
    public void testSetHeader() throws Exception {
        final Request req = Requests.get("test.com").setHeader("alpha", "beta");
        Assert.assertEquals("Set Header Mismatch", "beta", req.getHeader("alpha"));

    }

    @Test
    public void testAccept() throws Exception {
        final Request req = Requests.post("").accept("everything");
        Assert.assertEquals("Accept Header mismatch", "everything",
                req.getHeader(RequestHeaderFields.ACCEPT.getName()));

    }

    @Test
    public void testAcceptEncoding() throws Exception {
        final String encoding = "some encoding";
        final Request req = Requests.head("anything").acceptEncoding(encoding);
        Assert.assertEquals("Accept Encoding Header mismatch", encoding,
                req.getHeader(RequestHeaderFields.ACCEPT_ENCODING.getName()));
    }

    @Test
    public void testAcceptLanguage() throws Exception {
        final String language = "some language";
        final Request req = Requests.head("anything").acceptLanguage(language);
        Assert.assertEquals("Accept Language Header mismatch", language,
                req.getHeader(RequestHeaderFields.ACCEPT_LANGUAGE.getName()));
    }

    @Test
    public void testUserAgent() throws Exception {
        final String agent = "some agent";
        final Request req = Requests.head("anything").userAgent(agent);
        Assert.assertEquals("User Agent Header mismatch", agent,
                req.getHeader(RequestHeaderFields.USER_AGENT.getName()));

    }

    @Test
    public void testReferer() throws Exception {
        final String referer = "some referer";
        final Request req = Requests.put("anything").referer(referer);
        Assert.assertEquals("Referer Header mismatch", referer,
                req.getHeader(RequestHeaderFields.REFERER.getName()));

    }

    @Test
    public void testAuthorization() throws Exception {
        final String authorization = "some authorization";
        final Request req = Requests.put("anything").authorization(authorization);
        Assert.assertEquals("Authorization Header mismatch", authorization,
                req.getHeader(RequestHeaderFields.AUTHORIZATION.getName()));


    }

    @Test
    public void testAuthorize() {
        final String authorization = "Basic dXNlcm5hbWU6cGFzc3dvcmQ=";
        final Request req = Requests.get("anything").authorize("username", "password");
        Assert.assertEquals("USer Authorization mismatch", authorization,
                req.getHeader(RequestHeaderFields.AUTHORIZATION.getName()));


    }

    @Test
    public void testIfModifiedSince() throws Exception {
        final String ifmodsince = "some ifmodsince";
        final Request req = Requests.put("anything").ifModifiedSince(ifmodsince);
        Assert.assertEquals("If MOdified Since Header mismatch", ifmodsince,
                req.getHeader(RequestHeaderFields.IF_MODIFIED_SINCE.getName()));


    }

    @Test
    public void testPragmaNoCache() throws Exception {
        final String pragma = "no-cache";
        final Request req = Requests.get("anything").pragmaNoCache();
        Assert.assertEquals("Pragma Header mismatch", pragma,
                req.getHeader(RequestHeaderFields.PRAGMA.getName()));


    }

    @Test
    public void testSetParam() throws Exception {
        final String paramNme = "some param";
        final String paramValue1 = "some value1";
        final String paramValue2 = "some value2";
        final Request req = Requests.head("anything")
                .setParam(paramNme, paramValue1).setParam(paramNme, paramValue2);
        Assert.assertEquals("Param mismatch", paramValue1, req.getFirstParam(paramNme));
        Assert.assertEquals("Param mismatch", Arrays.asList(paramValue1, paramValue2), req.getParam(paramNme));
    }

    @Test
    public void testGetAndSetConnectionTimeOutMillis() {
        final int connectionTimeOutMillis = 10980;
        final Request req = Requests.post("something").connectionTimeOut(connectionTimeOutMillis);

        Assert.assertEquals("Connection Timeout mismatch", connectionTimeOutMillis,
                req.getConnectionTimeOutMillis());
    }

    @Test
    public void testGetAndSetSocketTimeOutMillis() {
        final int socketTimeOutMillis = 12100;
        final Request req = Requests.post("something").socketTimeOut(socketTimeOutMillis);
        Assert.assertEquals("socket Timeout mismatch", socketTimeOutMillis, req.getSocketTimeOutMillis());
    }

}
