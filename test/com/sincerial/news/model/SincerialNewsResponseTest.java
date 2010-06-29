package com.sincerial.news.model;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 23, 2010
 * Time: 12:08:00 PM
 * Tests the SincerialNewsResponse class from Sincerializer
 */
public class SincerialNewsResponseTest {
    Gson gson;
    List<NewsItem> newsItems;
    Sincerializer.SincerialNewsResponse newsResponse;
    String json;

    public static final String ID1 = "123";
    public static final String CATEGORY1 = "bullshit";
    public static final String AUTHOR1 = "anAuthor";
    public static final String MESSAGE1 = "is this a message";
    public static final String ID2 = "abc";
    public static final String CATEGORY2 = "bullcrap";
    public static final String AUTHOR2 = "anotherAuthor";
    public static final String MESSAGE2 = "then this must be a message too";
    public static final int HITS_REQUESTED = 26;
    public static final int TOTAL_HITS = 47;
    
    @Before
    public void setUp() {
        gson = new Gson();
        newsItems = new ArrayList<NewsItem>();
        newsItems.add(new NewsItem(ID1, CATEGORY1, AUTHOR1, MESSAGE1));
        newsItems.add(new NewsItem(ID2, CATEGORY2, AUTHOR2, MESSAGE2));
        newsResponse = new Sincerializer.SincerialNewsResponse(HITS_REQUESTED, TOTAL_HITS, newsItems);
        json = "{\"hits_requested\":" + HITS_REQUESTED + ",\"total_hits\":" + TOTAL_HITS + "," +
                "\"results\":" + gson.toJson(newsItems) + "}";

    }

    @Test
    public void create() {
        Assert.assertEquals(HITS_REQUESTED, newsResponse.hitsRequested);
        Assert.assertEquals(TOTAL_HITS, newsResponse.totalHits);
        Assert.assertEquals(newsItems, newsResponse.results);
    }

    @Test
    public void serialize() {
        Assert.assertEquals(json, gson.toJson(newsResponse));
    }

    @Test
    public void deSerialize() {
        Sincerializer.SincerialNewsResponse deserializedNewsResponse =
                gson.fromJson(json, Sincerializer.SincerialNewsResponse.class);
        Assert.assertEquals(HITS_REQUESTED, deserializedNewsResponse.hitsRequested);
        Assert.assertEquals(TOTAL_HITS, deserializedNewsResponse.totalHits);
        List<NewsItem> deserializedNewsItems = deserializedNewsResponse.results;
        for (int i = 0, l = newsItems.size(); i < l; ++i) {
            Assert.assertEquals(newsItems.get(i).getAuthor(), deserializedNewsItems.get(i).getAuthor());
            Assert.assertEquals(newsItems.get(i).getMessage(), deserializedNewsItems.get(i).getMessage());
            Assert.assertEquals(newsItems.get(i).getId(), deserializedNewsItems.get(i).getId());
        }
    }
}
