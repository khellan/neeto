/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 10, 2010
 * Time: 3:28:09 PM
 */

package com.sincerial.news.servlets;

import java.io.*;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.*;

import com.google.gson.Gson;

import com.sincerial.news.models.*;

/**
 * A servlet that collects your news and presents a view that is personalized for you
 */
public class Retriever extends HttpServlet {
    public static final int MAX_RETRIES = 3;
    public static final String VENDOR_ID = "vendor_id";
    public static final String USER_ID = "user_id";
    public static final String PASSWORD = "password";
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        int twitterRetries = MAX_RETRIES;
        int sincerialRetries = MAX_RETRIES;

        boolean twitterSuccess = false;
        boolean sincerialSuccess = false;

        List<NewsItem> twitterNews = Collections.emptyList();
        List<NewsItem> personalNews = Collections.emptyList();

        String vendorId = request.getParameter(VENDOR_ID);
        String userId = request.getParameter(USER_ID);
        String password = request.getParameter(PASSWORD);

        TweetRetriever retriever = new TweetRetriever();
        Sincerializer sincerializer = new Sincerializer();

        while (!twitterSuccess && twitterRetries > 0) {
            try {
                twitterNews = retriever.getUserTimeline(userId, password);
                twitterSuccess = true;
            } catch (RetrievalException e) {
                e.printStackTrace();
                --twitterRetries;
            }
        }

        System.out.println(MAX_RETRIES - twitterRetries + " retries for twitter, " + twitterNews.size() + " tweets");
        System.out.println(new Gson().toJson(twitterNews));

        while (twitterSuccess && !sincerialSuccess && sincerialRetries > 0) {
            try {
                personalNews = sincerializer.getRankedNews(twitterNews, vendorId + "_" + userId, "");
                sincerialSuccess = true;
            } catch (RetrievalException e) {
                e.printStackTrace();
                --sincerialRetries;
            }
        }

        System.out.println(MAX_RETRIES - sincerialRetries + " retries for Sincerial");
        
        String json = new Gson().toJson(personalNews);
        System.out.println(json);
        out.println(json);
    }
}
