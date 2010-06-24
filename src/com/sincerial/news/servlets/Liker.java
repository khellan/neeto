package com.sincerial.news.servlets;

import com.sincerial.news.models.NewsItem;
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
    public static final String CATEGORY = "category";
    public static final String MESSAGE = "message";
    public static final String AUTHOR = "author";
    public static final String LIKE = "like";
    public static final String VENDOR_ID = "vendor_id";
    public static final String USER_ID = "user_id";

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        String documentId = request.getParameter(DOCUMENT_ID);
        String category = request.getParameter(CATEGORY);
        String message = request.getParameter(MESSAGE);
        String author = request.getParameter(AUTHOR);
        boolean like = Boolean.parseBoolean(request.getParameter(LIKE));
        String vendorId = request.getParameter(VENDOR_ID);
        String userId = request.getParameter(USER_ID);

        NewsItem newsItem = new NewsItem(documentId, category, author, message);
        Sincerializer sincerializer = new Sincerializer();

        String sincerialUserId = vendorId + "_" + userId;
        boolean result = sincerializer.setLikedNews(newsItem, sincerialUserId, "", like);
        System.out.println("Id: " + documentId + " is liked? " + like + " and stored? " + result + " for " + sincerialUserId);

        out.println("{\"like\":" + like + "}");        
    }
}
