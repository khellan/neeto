package com.sincerial.news.models;

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
 * Time: 12:06:41 PM
 * Tests the SincerialNewsPayload class form Sincerializer
 */
public class SincerialNewsPayloadTest {
    Gson gson;
    List<NewsItem> newsItems;
    Sincerializer.SincerialNewsPayload newsPayload;
    String json;

    public static final String USER_ID = "madTester";
    public static final String PASSWORD = "banana";
    public static final String VENDOR_ID = "V";
    public static final String AUTHOR1 = "anAuthor";
    public static final String MESSAGE1 = "is this a message";
    public static final String AUTHOR2 = "anotherAuthor";
    public static final String MESSAGE2 = "then this must be a message too";

    @Before
    public void setUp() {
        gson = new Gson();
        newsItems = new ArrayList<NewsItem>();
        newsItems.add(new NewsItem(AUTHOR1, MESSAGE1));
        newsItems.add(new NewsItem(AUTHOR2, MESSAGE2));
        newsPayload = new Sincerializer.SincerialNewsPayload(USER_ID, PASSWORD, VENDOR_ID, newsItems);
        json = "{\"user_id\":\"" + USER_ID + "\",\"password\":\"" + PASSWORD + "\"," +
                "\"vendor_id\":\"" + VENDOR_ID + "\",\"news_items\":" + gson.toJson(newsItems) + "}";
    }

    @Test
    public void create() {
        Assert.assertEquals(USER_ID, newsPayload.userId);
        Assert.assertEquals(PASSWORD, newsPayload.password);
        Assert.assertEquals(VENDOR_ID, newsPayload.vendorId);
        Assert.assertEquals(newsItems, newsPayload.newsItems);
    }

    @Test
    public void serialize() {
        Assert.assertEquals(json, gson.toJson(newsPayload));
    }

    @Test
    public void deSerialize() {
        Sincerializer.SincerialNewsPayload deserializedNewsPayload =
                gson.fromJson(json, Sincerializer.SincerialNewsPayload.class);
        Assert.assertEquals(USER_ID, deserializedNewsPayload.userId);
        Assert.assertEquals(PASSWORD, deserializedNewsPayload.password);
        Assert.assertEquals(VENDOR_ID, deserializedNewsPayload.vendorId);
        List<NewsItem> deserializedNewsItems = deserializedNewsPayload.newsItems;
        for (int i = 0, l = newsItems.size(); i < l; ++i) {
            Assert.assertEquals(newsItems.get(i).getAuthor(), deserializedNewsItems.get(i).getAuthor());
            Assert.assertEquals(newsItems.get(i).getMessage(), deserializedNewsItems.get(i).getMessage());
        }
    }
}
