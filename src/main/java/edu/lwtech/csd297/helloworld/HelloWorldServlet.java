package edu.lwtech.csd297.helloworld;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import freemarker.template.*;
import org.apache.logging.log4j.*;

import edu.lwtech.csd297.helloworld.commands.*;

// World's Simplest HelloWorld Servlet -
//      http://server:8080/helloworld/servlet
//
// Chip Anderson
// LWTech CSD297

@WebServlet(name = "helloworld", urlPatterns = "/servlet", displayName = "HelloWorld servlet", loadOnStartup = 0)
public class HelloWorldServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(HelloWorldServlet.class);

    private static Configuration freeMarkerConfig = null;
    private static final Map<String, ServletCommand> supportedCommands = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws UnavailableException {
        logger.warn("=======  HelloWorld SERVLET INIT ========");

        logger.info("Initializing FreeMarker...");
        String templateDir = config.getServletContext().getRealPath("/WEB-INF/classes/templates");
        logger.debug("templatesDir = {}", templateDir);
        freeMarkerConfig = new Configuration(Configuration.VERSION_2_3_0);
        try {
            freeMarkerConfig.setDirectoryForTemplateLoading(new File(templateDir));
        } catch (IOException e) {
            throw new UnavailableException("Template directory not found");
        }
        logger.info("Successfully initialized FreeMarker.");

        // Load the supported cmd's into the Handler map
        supportedCommands.put("home", new HomeHandler());
        supportedCommands.put("showRandoms", new ShowRandomsHandler());
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

        // Get the cmd parameter from the URL
        String cmd = request.getParameter("cmd");
        if (cmd == null)
            cmd = "home";

        // Find the corresponding ServletCommand handler
        ServletCommand command = supportedCommands.get(cmd);
        if (command == null)
            command = supportedCommands.get("home");
        
        // Call the handler's handle() method to generate the HTML response
        String htmlPage = command.handle(request);

        // Send the HTML response to the user
        try (ServletOutputStream out = response.getOutputStream()) {
            out.println(htmlPage);
        } catch (IOException e) {
            logger.error("Unexpected IO Error sending response.", e);
        }

        long time = System.currentTimeMillis() - startTime;
        logger.info("OUT- {} {} {} {}ms", request.getRemoteAddr(), request.getMethod(), request.getRequestURI(), time);
    }

    public static Configuration getFreeMarkerConfig() {
        return freeMarkerConfig;
    }

}
