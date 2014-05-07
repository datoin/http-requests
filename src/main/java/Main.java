import org.datoin.net.http.Requests;

import java.io.FileNotFoundException;

/**
 * Author : umarshah@simplyphi.com
 * Created on : 6/5/14.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        /*System.out.println(Requests.post("http://node5:8090/falcon/hatch")
                .setParam("userId", "1")
                .setParam("className", "com.datoin.modules.example.IdentityModule")
                .socketTimeOut(100000).addInputStream("file", new File(
                        "/home/umar/.m2/repository/com/datoin/modules/examples/identity-module/1.0.1/identity-module-1.0.1.jar"))
                .execute());
        */
        System.out.println(Requests.head("http://techcrunch.com").acceptEncoding("x-compress; x-zip")
                .ifModifiedSince("Wed, 07 May 2014 18:22:07 GMT").socketTimeOut(100000)
                .connectionTimeOut(1000).execute());
        //System.out.println(Requests.get("http://node5:8090/jeda/pman/v1/modules").setConnectionTimeOutMillis(1000).execute());
    }
}
