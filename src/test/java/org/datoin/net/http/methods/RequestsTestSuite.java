package org.datoin.net.http.methods;

/**
 * Author : umarshah@simplyphi.com
 * Created on : 25/6/14.
 */

import org.datoin.net.http.TestServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({DeleteTest.class, HeadTest.class,
        GetTest.class, PutTest.class, PostTest.class})
public class RequestsTestSuite {
    static final int PORT = 6666;
    static Server server = null;
    static final String CONTEXT = "/datoin";
    private static Server getJettyServer(int port) {
        Server server = null;
        if(server == null) {
            server = new Server(port);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath(CONTEXT);
            server.setHandler(context);
            context.addServlet(new ServletHolder(new TestServlet()), "/*");
        }
        return server;
    }

    @BeforeClass
    public static void startServer() throws Exception {
        System.out.println("HTTP Requests Tests STARTED...");
        System.out.println("Starting JETTY Server on PORT: " + PORT);
        server = getJettyServer(PORT);
        server.start();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        System.out.println("Stopping JETTY Server on PORT: " + PORT);
        if (server != null){
            server.stop();
        }
        System.out.println("HTTP Requests Tests completed...");
    }
}
