package org.datoin.net.http.methods;

import org.apache.http.entity.ContentType;
import org.datoin.net.http.HTTPRequestTest;
import org.datoin.net.http.RequestHeaderFields;
import org.datoin.net.http.Requests;
import org.datoin.net.http.Response;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PutTest extends HTTPRequestTest {
    public static final int PORT = 6667;
    private static Server server = null;
    @BeforeClass
    public static void startServer() throws Exception {
        System.out.println("Starting JETTY Server for PUT");
        server = getJettyServer(PORT);
        server.start();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        if (server != null){
            System.out.println("Stopping JETTY Server");
            server.stop();
        }
    }

    @Test
    public void testExecute() throws Exception {
        String url = String.format("http://localhost:%s/%s/", PORT, CONTEXT);
        String headerValue = "text/plain";
        String userAgentValue = "mytest/1.0";
        Response response = Requests.put(url)
                .accept(headerValue)
                .userAgent(userAgentValue)
                .execute();
        InputStream input= new ByteArrayInputStream(response.getContentAsByteArray());
        Properties props = new Properties();
        props.load(input);
        System.out.print(props);
        Object o = props.get(RequestHeaderFields.USER_AGENT.getName());
        Assert.assertEquals("UserAgent didnt match", userAgentValue, o.toString().trim());
        Object o1 = props.get(RequestHeaderFields.ACCEPT.getName());
        Assert.assertEquals("Accept header didnt match", headerValue, o1.toString().trim());
        Assert.assertEquals("Method didnt match", "PUT", props.get("Method").toString().trim());


    }

    @Test
    public void testPutContent() throws Exception {
        String url = String.format("http://localhost:%s/%s/", PORT, CONTEXT);
        String testText = "this is a test text";
        InputStream is = new ByteArrayInputStream(testText.getBytes());
        Response response = Requests.put(url).
                setContent(is, ContentType.TEXT_PLAIN).execute();

        InputStream input= new ByteArrayInputStream(response.getContentAsByteArray());
        Properties props = new Properties();
        props.load(input);
        System.out.print(props);
        Assert.assertEquals("Method didnt match", "PUT", props.get("Method").toString().trim());
        Assert.assertEquals("content text didnt match", testText, props.get("content").toString().trim());
    }
}