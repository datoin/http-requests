package org.datoin.net.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Author : umarshah@simplyphi.com
 * Created on : 16/5/14.
 */
public class HTTPRequestTest {
    public static final String CONTEXT = "/datoin";
    protected static Server getJettyServer(int port) {
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
}
