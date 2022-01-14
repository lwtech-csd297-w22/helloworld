package edu.lwtech.csd297.helloworld;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.apache.logging.log4j.*;

// World's Simplest HelloWorld Servlet -
//      http://server:8080/helloworld/servlet
//
// Chip Anderson
// LWTech CSD297

@WebServlet(name = "helloworld", urlPatterns = "/servlet", displayName = "HelloWorld servlet", loadOnStartup = 0)
public class HelloWorldServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(HelloWorldServlet.class);

    @Override
    public void init(ServletConfig config) {
        logger.warn("=======  HelloWorld SERVLET INIT ========");
    }

    @Override
    public void destroy() {
        logger.warn("------  HelloWorld SERVLET DESTROY ------");
        logger.warn("");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        logger.debug("IN - {} {} {}", request.getRemoteAddr(), request.getMethod(), request.getRequestURI());

        try (ServletOutputStream out = response.getOutputStream()) {
            out.println("<html><body><h1>Hello World!</h1></body></html>");
        } catch (IOException e) {
            logger.error("Unexpected IO Error sending response.", e);
        }

        long time = System.currentTimeMillis() - startTime;
        logger.info("OUT- {} {} {} {}ms", request.getRemoteAddr(), request.getMethod(), request.getRequestURI(), time);
    }

}
