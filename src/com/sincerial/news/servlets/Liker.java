package com.sincerial.news.servlets;

import com.sincerial.news.models.Sincerializer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 23, 2010
 * Time: 3:26:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Liker extends HttpServlet {
    public static final String DOCUMENT_ID = "document_id";
    public static final String LIKE = "like";
    public static final int MAX_RETRIES = 3;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        String documentId = request.getParameter(DOCUMENT_ID);
        boolean like = Boolean.parseBoolean(request.getParameter(LIKE));

        Sincerializer sincerializer = new Sincerializer();
        int retries = MAX_RETRIES;

        boolean result = sincerializer.setLikedNews(documentId, like);
        System.out.println("Id: " + documentId + " is liked? " + like + " and stored? " + result);

        out.println("{\"like\":" + like + "}");        
    }
}
