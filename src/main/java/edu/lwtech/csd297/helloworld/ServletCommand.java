package edu.lwtech.csd297.helloworld;

import javax.servlet.http.*;

public interface ServletCommand {
    String handle(HttpServletRequest request);    
}
