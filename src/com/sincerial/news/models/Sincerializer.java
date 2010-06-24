package com.sincerial.news.models;

/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 11, 2010
 * Time: 1:35:57 PM
 * 
 *  Handles communication with the Sincerial servers
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Class responsible for all communcation with Sincerial
 */
public class Sincerializer {

    public static final String URL = "http://localhost:3000/";
    public static final String NEWS_RANK_PATH = "rank/news";
    public static final String LIKE_PATH = "report_interest";
    public static final String VENDOR_ID = "301";
    public static final int CONNECTION_TIMEOUT = 5000;
    public static final String ENCODING = "UTF-8";

    /**
     * Class responsible for being turend into a JSON hash to be eaten by the Sincerial servers
     */
    public static class SincerialNewsPayload {
        @SerializedName("user_id") String userId;
        String password;
        @SerializedName("vendor_id") String vendorId;
        @SerializedName("news_items") List<NewsItem> newsItems;

        /**
         * No-args constructor for Gson deserialization
         */
        SincerialNewsPayload() {}

        SincerialNewsPayload(String userId, String password, String vendorId, List<NewsItem> newsItems) {
            this.userId = userId;
            this.password = password;
            this.vendorId = vendorId;
            this.newsItems = newsItems;
        }
    }

    public static class SincerialNewsResponse {
        @SerializedName("hits_requested") int hitsRequested;
        @SerializedName("total_hits") int totalHits;
        List<NewsItem> results;

        SincerialNewsResponse(int hitsRequested, int totalHits, List<NewsItem> results) {
            this.hitsRequested = hitsRequested;
            this.totalHits = totalHits;
            this.results = results;
        }

        /**
         * No-args constructor for Gson deserialization
         */
        SincerialNewsResponse() {}
    }

    public List<NewsItem> getRankedNews(List<NewsItem> newsItems, String userId, String password)
            throws RetrievalException, IOException {
        String url = URL + NEWS_RANK_PATH;
        Gson gson = new Gson();
        SincerialNewsPayload payload = new SincerialNewsPayload(userId, password, VENDOR_ID, newsItems);

        try {
            URL requestURL = new URL(url);
            URLConnection connection = requestURL.openConnection();

            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(CONNECTION_TIMEOUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

            writer.write("payload=" + URLEncoder.encode(gson.toJson(payload), ENCODING));
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String json = reader.readLine();
            System.out.println("Read <<<" + json + ">>>");
            //SincerialNewsResponse response = gson.fromJson(reader, SincerialNewsResponse.class);
            SincerialNewsResponse response = gson.fromJson(json, SincerialNewsResponse.class);

            return response.results;
        } catch (MalformedURLException e) {
            throw new RetrievalException("URL didn't turn out right: " + url, e.getCause());
        }
    }

    public boolean setLikedNews(NewsItem newsItem, String userId, String password, boolean like) throws IOException {
        String url = URL + LIKE_PATH + "/" + VENDOR_ID + "/" + userId;
        Gson gson = new Gson();
        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        newsItems.add(newsItem);
        SincerialNewsPayload payload = new SincerialNewsPayload(userId, password, VENDOR_ID, newsItems);

        URL requestURL = new URL(url);
        URLConnection connection = requestURL.openConnection();

        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(CONNECTION_TIMEOUT);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

        writer.write("payload=" + URLEncoder.encode(gson.toJson(payload), ENCODING));
        writer.flush();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = reader.readLine();

        System.out.println("Read <<<" + response + ">>>");

        return response.equals("OK");
    }
}
