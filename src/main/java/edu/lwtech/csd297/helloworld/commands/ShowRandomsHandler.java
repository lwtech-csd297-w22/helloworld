package edu.lwtech.csd297.helloworld.commands;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;

import freemarker.template.*;
import org.apache.logging.log4j.*;

import edu.lwtech.csd297.helloworld.*;

public class ShowRandomsHandler implements ServletCommand {

    private static final Logger logger = LogManager.getLogger(ShowRandomsHandler.class);
    
    // Handle the "showRandoms" command
    @Override
    public String handle(HttpServletRequest request) {

        String templateFile = "randoms.ftl";
        Map<String, Object> templateFields = new HashMap<>();

        List<Double> randomDoubles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            randomDoubles.add(Math.random());
        }
        templateFields.put("randomValues", randomDoubles);

        StringWriter processedTemplate = new StringWriter();
        try {
            Template view = HelloWorldServlet.getFreeMarkerConfig().getTemplate(templateFile);
            view.process(templateFields, processedTemplate);
        } catch (IOException | TemplateException e) {
            logger.error("Unexpected error processing Freemarker template: {}", templateFile, e);
        }

        return processedTemplate.toString();
    }

}
