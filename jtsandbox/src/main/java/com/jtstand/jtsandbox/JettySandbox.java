/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand.jtsandbox;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;

/**
 *
 * @author albert_kurucz
 */
public class JettySandbox {

    public static final void main(String[] args) {
        Handler handler = new AbstractHandler() {

            public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
                    throws IOException, ServletException {
                response.setContentType("text/html");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("<h1>Hello</h1>");
                ((Request) request).setHandled(true);
            }
        };

        Server server = new Server(8080);
        server.setHandler(handler);
        try {
            server.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
