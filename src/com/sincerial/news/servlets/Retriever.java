/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 10, 2010
 * Time: 3:28:09 PM
 */

package com.sincerial.news.servlets;

import javax.servlet.http.*;
import java.io.*;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;

import com.sincerial.news.models.RetrievalException;
import com.sincerial.news.models.NewsItem;
import com.sincerial.news.models.TweetRetriever;

/**
 * A servlet that collects your news and presents a view that is personalized for you
 */
public class Retriever extends HttpServlet {
    public static final int MAX_RETRIES = 3;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        int retries = MAX_RETRIES;
        boolean success = false;
        List<NewsItem> news = Collections.emptyList(); 
        TweetRetriever retriever = new TweetRetriever();
        while (!success && retries > 0) {
            try {
                news = retriever.getPublicTimeline();
                success = true;
            } catch (RetrievalException e) {
                e.printStackTrace();
                --retries;
            }
        }

        String json = new Gson().toJson(news);
        System.out.println(json);
        out.println(json);
    }
}
