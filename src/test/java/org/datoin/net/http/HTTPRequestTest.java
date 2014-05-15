package org.datoin.net.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Author : umarshah@simplyphi.com
 * Created on : 16/5/14.
 */
public class HTTPRequestTest {
    public static final int PORT = 6666;
    public static final String CONTEXT = "/datoin";
    private static Server server = null;

    @BeforeClass
    public static void startServer() throws Exception {
        System.out.println("Starting JETTY Server");
        server = getJettyServer();
        server.start();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        if (server!= null){
            System.out.println("Stopping JETTY Server");
            server.stop();
        }
    }

    private static Server getJettyServer() {
        Server server = new Server(PORT);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(CONTEXT);
        server.setHandler(context);
        context.addServlet(new ServletHolder(new TestServlet()), "/*");
        /*NCSARequestLog requestLog = new NCSARequestLog();
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        requestLogHandler.setRequestLog(requestLog);
        server.setHandler(requestLogHandler);
        server.setStopTimeout(2000);*/
        return server;
    }
}
