package com.sincerial.news.models;

/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 23, 2010
 * Time: 11:25:20 AM
 * Tests for the NewsItem class
 */

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NewsItemTest {
    Gson gson;
    NewsItem newsItem;
    Map<String, String> hyperlinks;
    String json;

    public static final String AUTHOR = "anAuthor";
    public static final String MESSAGE = "is this a message";
    public static final String ANCHOR_TEXT = "this";
    public static final String HYPERLINK = "http://test.com/";
    public static final String ID = "1234567890";
    public static final String CATEGORY = "news";

    @Before
    public void setUp() throws MalformedURLException {
        gson = new Gson();
        hyperlinks = new HashMap<String, String>();
        hyperlinks.put(ANCHOR_TEXT, HYPERLINK);
        newsItem = new NewsItem(ID, CATEGORY, AUTHOR, MESSAGE, hyperlinks);
        json = "{\"product_id\":\"" + ID + "\",\"category\":\"" + CATEGORY + "\"," +
                "\"author\":\"" + AUTHOR +"\",\"message\":\"" + MESSAGE + "\"," +
                "\"hyperlinks\":{\"" + ANCHOR_TEXT + "\":\"" + HYPERLINK + "\"}}";
    }

    @Test
    public void create() {
        Assert.assertEquals(ID, newsItem.getId());
        Assert.assertEquals(CATEGORY, newsItem.getCategory());
        Assert.assertEquals(AUTHOR, newsItem.getAuthor());
        Assert.assertEquals(MESSAGE, newsItem.getMessage());
        Assert.assertEquals(hyperlinks, newsItem.getHyperlinks());
    }

    @Test
    public void serialize() {
        Assert.assertEquals(json, gson.toJson(newsItem));
    }

    @Test
    public void deSerialize() {
        NewsItem deserializedNewsItem = gson.fromJson(json, NewsItem.class);
        Assert.assertEquals(ID, deserializedNewsItem.getId());
        Assert.assertEquals(CATEGORY, deserializedNewsItem.getCategory());
        Assert.assertEquals(AUTHOR, deserializedNewsItem.getAuthor());
        Assert.assertEquals(MESSAGE, deserializedNewsItem.getMessage());
        Assert.assertEquals(hyperlinks, deserializedNewsItem.getHyperlinks());
    }
}
