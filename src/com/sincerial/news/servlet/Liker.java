package com.sincerial.news.servlet;

import com.sincerial.news.listener.ServletParameterMapper;
import com.sincerial.news.model.NewsItem;
import com.sincerial.news.model.Sincerializer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 23, 2010
 * Time: 3:26:35 PM
 * Servlet responsible for handling like an unlike requests
 */
public class Liker extends HttpServlet {
    public static final String PRODUCT_ID = "product_id";
    public static final String CATEGORY = "category";
    public static final String MESSAGE = "message";
    public static final String AUTHOR = "author";
    public static final String LIKE = "like";
    public static final String VENDOR_ID = "vendor_id";
    public static final String USER_ID = "user_id";

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        String productId = request.getParameter(PRODUCT_ID);
        String category = request.getParameter(CATEGORY);
        String message = request.getParameter(MESSAGE);
        String author = request.getParameter(AUTHOR);
        boolean like = Boolean.parseBoolean(request.getParameter(LIKE));
        String vendorId = request.getParameter(VENDOR_ID);
        String userId = request.getParameter(USER_ID);

        NewsItem newsItem = new NewsItem(productId, category, author, message);
        @SuppressWarnings("unchecked")
        Sincerializer sincerializer = new Sincerializer(
                (Map<String, String>)getServletContext().getAttribute(ServletParameterMapper.PARAMETER_MAP_NAME));

        String sincerialUserId = vendorId + "_" + userId;
        System.out.println("What's to like?");
        boolean result = sincerializer.setLikedNews(newsItem, sincerialUserId, "", like);
        System.out.println("Id: " + productId + " is liked? " + like + " and stored? " + result + " for " + sincerialUserId);

        out.println("{\"like\":" + like + "}");        
    }
}
