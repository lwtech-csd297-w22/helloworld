package edu.lwtech.csd297.helloworld.commands;

import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;
import javax.servlet.http.*;

import freemarker.template.*;
import org.apache.logging.log4j.*;

import edu.lwtech.csd297.helloworld.*;

public class HomeHandler implements ServletCommand {

    private static final Logger logger = LogManager.getLogger(HomeHandler.class);
    
    // Handle the "home" command
    @Override
    public String handle(HttpServletRequest request) {

        String templateFile = "home.ftl";
        Map<String, Object> templateFields = new HashMap<>();

        templateFields.put("name", "Chip");
        ZonedDateTime currentTime = ZonedDateTime.now();
        templateFields.put("time", currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        templateFields.put("date", currentTime.format(DateTimeFormatter.ofPattern("LLLL d, yyyy")));

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
