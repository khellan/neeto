package com.sincerial.news.model;

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
import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Class responsible for all communcation with Sincerial
 */
public class Sincerializer {
    public static final String URL_PARAMETER = "SincerialUrl";
    public static final String NEWS_RANK_PATH_PARAMETER = "SincerialNewsRankPath";
    public static final String LIKE_PATH_PARAMETER = "SincerialLikePath";
    public static final String VENDOR_ID_PARAMETER = "SincerialVendorId";
    public static final String CONNECTION_TIMEOUT_PARAMETER = "SincerialConnectionTimeout";
    public static final String READ_TIMEOUT_PARAMETER = "SincerialReadTimeout";

    public static final String ENCODING = "UTF-8";

    protected Map<String, String> parameterMap;

    /**
     * Class responsible for being turend into a JSON hash to be eaten by the Sincerial servers
     */
    public static class SincerialNewsPayload {
        @SerializedName("user_id") String userId;
        String password;
        @SerializedName("vendor_id") String vendorId;
        @SerializedName("news_items") List<NewsItem> newsItems;
        @SerializedName("novelty_time") long noveltyTime;

        /**
         * No-args constructor for Gson deserialization
         */
        SincerialNewsPayload() {}

        SincerialNewsPayload(String userId, String password, String vendorId, List<NewsItem> newsItems, long noveltyTime) {
            this.userId = userId;
            this.password = password;
            this.vendorId = vendorId;
            this.newsItems = newsItems;
            this.noveltyTime = noveltyTime;
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

    public Sincerializer(Map<String, String> parameterMap) {
        this.parameterMap = parameterMap;
    }

    /**
     * Sends the {@link NewsItem}s to Sincerial for ranking
     *
     * @param newsItems A {@link List} of {@link NewsItem}s to rank
     * @param userId The Sincerial user id to rank for as a {@link String}
     * @param password The Sincerial password of the user {@link String}
     * @param noveltyTime The time constant for novelty ranking
     * @return The {@link List} of {@link NewsItem}s ranked for the user by Sincerial
     * @throws RetrievalException If there are communication issues with the Sincerial system
     * @throws IOException If there are problems with the response from the Sincerial system
     */
    public List<NewsItem> getRankedNews(List<NewsItem> newsItems, String userId, String password, long noveltyTime)
            throws RetrievalException, IOException {
        String url = parameterMap.get(URL_PARAMETER) + parameterMap.get(NEWS_RANK_PATH_PARAMETER);
        Gson gson = new Gson();
        SincerialNewsPayload payload = new SincerialNewsPayload(
                userId, password, parameterMap.get(VENDOR_ID_PARAMETER), newsItems, noveltyTime);

        Logger logger = Logger.getLogger(Sincerializer.class.getPackage().getName());
        try {
            URL requestURL = new URL(url);
            URLConnection connection = requestURL.openConnection();

            connection.setConnectTimeout(Integer.parseInt(parameterMap.get(CONNECTION_TIMEOUT_PARAMETER)));
            connection.setReadTimeout(Integer.parseInt(parameterMap.get(READ_TIMEOUT_PARAMETER)));
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write("payload=" + URLEncoder.encode(gson.toJson(payload), ENCODING));
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String json = reader.readLine();
            logger.fine("Read <<<" + json + ">>>");
            //SincerialNewsResponse response = gson.fromJson(reader, SincerialNewsResponse.class);
            SincerialNewsResponse response = gson.fromJson(json, SincerialNewsResponse.class);

            return response.results;
        } catch (MalformedURLException e) {
            throw new RetrievalException("URL didn't turn out right: " + url, e);
        } catch (java.net.SocketTimeoutException e) {
            logger.warning("Sincerial timed out");
            return newsItems;
        }
    }

    /**
     * Reports like to the Sincerial system
     * @param newsItem A {@link NewsItem} to report like for
     * @param userId The Sincerial user id to report like for as a {@link String}
     * @param password The Sincerial password of the user {@link String}
     * @param like Whether this is a like (true) or unlike (false)
     * @return Whether the reporting to Sincerial was a success (true is good, false not so)
     * @throws IOException If there are problems with the response from the Sincerial system
     */
    public boolean setLikedNews(NewsItem newsItem, String userId, String password, boolean like)
            throws IOException {
        String url = parameterMap.get(URL_PARAMETER) + parameterMap.get(LIKE_PATH_PARAMETER) + "/" +
                parameterMap.get(VENDOR_ID_PARAMETER) + "/" + userId;
        Gson gson = new Gson();
        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        newsItems.add(newsItem);
        SincerialNewsPayload payload = new SincerialNewsPayload(
                userId, password, parameterMap.get(VENDOR_ID_PARAMETER), newsItems, 0);
        Logger logger = Logger.getLogger(Sincerializer.class.getPackage().getName());

        URL requestURL = new URL(url);
        URLConnection connection = requestURL.openConnection();

        connection.setConnectTimeout(Integer.parseInt(parameterMap.get(CONNECTION_TIMEOUT_PARAMETER)));
        connection.setReadTimeout(Integer.parseInt(parameterMap.get(READ_TIMEOUT_PARAMETER)));
        connection.setDoInput(true);
        connection.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

        writer.write("payload=" + URLEncoder.encode(gson.toJson(payload), ENCODING));
        writer.flush();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = reader.readLine();

        logger.fine("Read <<<" + response + ">>>");

        return response.equals("OK");
    }
}
