import org.datoin.net.http.Requests;
import org.datoin.net.http.Response;

import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * Author : umarshah@simplyphi.com
 * Created on : 6/5/14.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        String hatchUrl = "http://node5:8090/falcon/hatch";
        final Response response = Requests.delete("http://localhost:8181/datoin/")
                .setParam("userId", "1")
                .accept("application/json").accept("text/plain")
                .userAgent("myself-test/1.0")
                .setParam("className", "com.datoin.modules.example.IdentityModule")
                .socketTimeOut(100000)
          /*      .addInputStream("file", new File(
                        "/home/umar/.m2/repository/com/datoin/modules/examples/identity-module/1.0.1/identity-module-1.0.1.jar"))*/
                .execute();
        System.out.println(response);
        System.out.println(response.getContentAsString());
        System.out.println(Arrays.toString(response.getContentAsByteArray()));
        /*
        final Response response = Requests.get("http://datoin.com").execute();
        System.out.println(response);
        System.out.println(response.getContentAsString());
        System.out.println(Arrays.toString(response.getContentAsByteArray()));
        */
        //System.out.println(response.getContentAsString());
        /*System.out.println(Requests.head("http://techcrunch.com").acceptEncoding("x-compress; x-zip")
                .ifModifiedSince("Wed, 07 May 2014 18:22:07 GMT").socketTimeOut(100000)
                .connectionTimeOut(1000).execute());
        */
        //System.out.println(Requests.get("http://node5:8090/jeda/pman/v1/modules").setConnectionTimeOutMillis(1000).execute());
        /*System.out.println(Requests.get("http://192.168.1.134:8090/jeda/dci/v1/jobs")
                .authorize("username", "password")
                //.authorization("Basic amVkYTpwYXNzd29yZA==")
                .execute());*/
    }
}
