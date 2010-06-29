package com.sincerial.news.listener;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 29, 2010
 * Time: 2:28:59 PM
 * Class used as a Module to encapsulate common behavior
 */
public class ServletParameterMapper implements ServletContextListener {
    public static final String PARAMETER_MAP_NAME = "parameterMap";
    
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        Map<String, String> parameterMap = getParameterMap(context);
        context.setAttribute(PARAMETER_MAP_NAME, parameterMap);
    }
    
    public void contextDestroyed(ServletContextEvent evet) {}

    /**
     * Converts the parameters in a {@link ServletContext} into a {@link Map}<{@link String}, {@link String}>
     * @param context The {@link ServletContext} to convert
     * @return A {@link Map}<{@link String}, {@link String}> og parameter name to value
     */
    public Map<String, String> getParameterMap(ServletContext context) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        for (Enumeration e = context.getInitParameterNames(); e.hasMoreElements();) {
            String parameterName = (String)e.nextElement();
            parameterMap.put(parameterName, context.getInitParameter(parameterName));
        }
        return parameterMap;
    }
}
