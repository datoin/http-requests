package org.datoin.net.http.methods;

import org.datoin.net.http.Requests;
import org.datoin.net.http.Response;
import org.junit.Assert;
import org.junit.Test;

public class HeadTest {

    @Test
    public void testExecute() throws Exception {
        String url = String.format("http://localhost:%s/%s/", RequestsTestSuite.PORT, RequestsTestSuite.CONTEXT);

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