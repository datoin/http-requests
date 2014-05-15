package org.datoin.net.http;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Author : umarshah@simplyphi.com
 * Created on : 15/5/14.
 */
// Extend HttpServlet class
public class TestServlet extends HttpServlet {

    private String message;

    public void init() throws ServletException {
        // Do required initialization
        message = "Hello Datoin World";
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)

            throws ServletException, IOException {
        handleRequest(request, response, "POST");
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response, "GET");
    }

    public void doPut(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response, "PUT");
    }


    public void doHead(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response, "HEAD");
    }

    public void doDelete(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response, "DELETE");
    }

    private void handleRequest(HttpServletRequest request,
                               HttpServletResponse response, String method) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");
        response.setStatus(200);

        Enumeration<String> headerNames = request.getHeaderNames();

        StringBuilder sb = new StringBuilder("Method : ").append(method).append("\n");
        while (headerNames.hasMoreElements()) {

            String headerName = headerNames.nextElement();
            sb.append(headerName).append(" : ");

            Enumeration<String> headers = request.getHeaders(headerName);
            while (headers.hasMoreElements()) {
                String headerValue = headers.nextElement();
                sb.append(headerValue).append(" ");
            }
            sb.append("\n\r");
        }
        sb.append("content : ");
        BufferedReader reader = request.getReader();
        String line;
        while((line = reader.readLine())!= null){
            sb.append(line.trim());
        }
        sb.append("\n");
        out.write(sb.toString());
        out.close();

    }

    public void destroy() {
        // do nothing.
    }

    public static void main(String[] args) throws Exception {




    }
}