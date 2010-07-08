/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 10, 2010
 * Time: 3:28:09 PM
 */

package com.sincerial.news.servlet;

import java.io.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.*;

import com.google.gson.Gson;

import com.google.gson.annotations.SerializedName;
import com.sincerial.news.listener.ServletParameterMapper;
import com.sincerial.news.model.*;

/**
 * A servlet that collects your news and presents a view that is personalized for you
 */
public class Retriever extends HttpServlet {
    public static final int MAX_RETRIES = 3;
    public static final String VENDOR_ID = "vendor_id";

    public static class NewsItemPackage {
        @SerializedName("signed_in") boolean signedIn;
        List<NewsItem> items;
        long timestamp;

        NewsItemPackage() {}

        NewsItemPackage(boolean signedIn, List<NewsItem> newsItems, long timestamp) {
            this.signedIn = signedIn;
            this.items = newsItems;
            this.timestamp = timestamp;
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        int twitterRetries = MAX_RETRIES;
        int sincerialRetries = MAX_RETRIES;

        boolean twitterSuccess = false;
        boolean sincerialSuccess = false;

        List<NewsItem> twitterNews = Collections.emptyList();

        HttpSession session = request.getSession();

        String vendorId = request.getParameter(VENDOR_ID);
        @SuppressWarnings("unchecked")
        String userId = (String)session.getAttribute(SignIn.USER_ID);
        boolean signedIn = (userId != null) && !"".equals(userId);
        @SuppressWarnings("unchecked")
        String password = (String)session.getAttribute(SignIn.PASSWORD);

        Logger logger = Logger.getLogger(Retriever.class.getPackage().getName());
        TweetRetriever retriever = new TweetRetriever();
        while (!twitterSuccess && twitterRetries > 0) {
            try {
                if (signedIn) {
                    twitterNews = retriever.getUserTimeline(userId, password);
                } else {
                    twitterNews = retriever.getPublicTimeline();
                }
                twitterSuccess = true;
            } catch (RetrievalException e) {
                logger.info("Barfing");
                logger.log(Level.WARNING, "RetrievalException from Twitter", e);
                --twitterRetries;
            }
        }

        logger.info(MAX_RETRIES - twitterRetries + " retries for twitter, " + twitterNews.size() + " tweets");
        logger.fine(new Gson().toJson(twitterNews));
        @SuppressWarnings("unchecked")
        Sincerializer sincerializer = new Sincerializer(
                (Map<String, String>)getServletContext().getAttribute(ServletParameterMapper.PARAMETER_MAP_NAME));

        List<NewsItem> personalNews = twitterNews;
        while (twitterSuccess && !sincerialSuccess && sincerialRetries > 0 && !"".equals(userId)) {
            try {
                personalNews = sincerializer.getRankedNews(twitterNews, vendorId + "_" + userId, "");
                sincerialSuccess = true;
            } catch (RetrievalException e) {
                logger.log(Level.WARNING, "RetrievalException from Sincerial", e);
                --sincerialRetries;
            } catch (IOException e) {
                logger.severe("IO Error talking to Sincerial");
                --sincerialRetries;
            }
        }

        logger.info(MAX_RETRIES - sincerialRetries + " retries for Sincerial");

        NewsItemPackage newsItemPackage = new NewsItemPackage(signedIn, personalNews, (new Date()).getTime());
        String json = new Gson().toJson(newsItemPackage);
        logger.fine(json);
        out.println(json);
    }
}
