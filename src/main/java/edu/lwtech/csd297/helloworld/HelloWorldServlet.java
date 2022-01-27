package edu.lwtech.csd297.helloworld;

import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import freemarker.template.*;
import org.apache.logging.log4j.*;

// World's Simplest HelloWorld Servlet -
//      http://server:8080/helloworld/servlet
//
// Chip Anderson
// LWTech CSD297

@WebServlet(name = "helloworld", urlPatterns = "/servlet", displayName = "HelloWorld servlet", loadOnStartup = 0)
public class HelloWorldServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(HelloWorldServlet.class);

    private static Configuration freeMarkerConfig = null;

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

        String cmd = request.getParameter("cmd");
        if (cmd == null)
            cmd = "home";

        String templateFile = "";
        Map<String, Object> templateFields = new HashMap<>();

        switch (cmd) {
            case "home":
                templateFile = "home.ftl";

                templateFields.put("name", "CSD 297");
                ZonedDateTime currentTime = ZonedDateTime.now();
                templateFields.put("time", currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                templateFields.put("date", currentTime.format(DateTimeFormatter.ofPattern("LLLL d, yyyy")));
                break;

            case "showRandoms":
                templateFile = "randoms.ftl";

                List<Double> randomDoubles = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    randomDoubles.add(Math.random());
                }
                templateFields.put("randomValues", randomDoubles);
                break;

            default:
                templateFile = "home.ftl";
                break;
        }
        
        // Merge the template with the data map
        String htmlPage = processTemplate(templateFile, templateFields);

        // Send the resulting HTML to the user
        try (ServletOutputStream out = response.getOutputStream()) {
            out.println(htmlPage);
        } catch (IOException e) {
            logger.error("Unexpected IO Error sending response.", e);
        }

        long time = System.currentTimeMillis() - startTime;
        logger.info("OUT- {} {} {} {}ms", request.getRemoteAddr(), request.getMethod(), request.getRequestURI(), time);
    }

    // ------------------------------------------------------------------

    private String processTemplate(String templateFile, Map<String, Object> templateFields) {
        StringWriter processedTemplate = new StringWriter();
        try {
            Template view = freeMarkerConfig.getTemplate(templateFile);
            view.process(templateFields, processedTemplate);
        } catch (IOException | TemplateException e) {
            logger.error("Unexpected error processing Freemarker template: {}", templateFile, e);
        }
        return processedTemplate.toString();
    }

}
