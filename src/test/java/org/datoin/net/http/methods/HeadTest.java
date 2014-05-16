package org.datoin.net.http.methods;

import org.datoin.net.http.HTTPRequestTest;
import org.datoin.net.http.Requests;
import org.datoin.net.http.Response;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class HeadTest extends HTTPRequestTest {
    public static final int PORT = 6664;
    private static Server server = null;
    @BeforeClass
    public static void startServer() throws Exception {
        System.out.println("Starting JETTY Server for GET");
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

        String userAgentValue = "mytest/1.0";
        Response response = Requests.head(url)
                .userAgent(userAgentValue)
                .execute();
        Assert.assertNull("response should be null for head", response.getContentAsByteArray());
        Assert.assertEquals("response should be 200", 200, response.getStatus());
        System.out.println(response.getRequestLine());
        Assert.assertEquals("method should be head", Methods.HEAD.getMethod(), response.getMethod());


    }
}