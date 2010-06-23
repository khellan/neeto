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

public class NewsItemTest {
    Gson gson;
    NewsItem newsItem;

    public static final String AUTHOR = "anAuthor";
    public static final String MESSAGE = "is this a message";

    @Before
    public void setUp() {
        gson = new Gson();
        newsItem = new NewsItem(AUTHOR, MESSAGE);
    }

    @Test
    public void create() {
        Assert.assertEquals(AUTHOR, newsItem.getAuthor());
        Assert.assertEquals(MESSAGE, newsItem.getMessage());
    }

    @Test
    public void serialize() {
        String expected = "{\"author\":\"" + AUTHOR +"\",\"message\":\"" + MESSAGE + "\"}";
        Assert.assertEquals(expected, gson.toJson(newsItem));
    }

    @Test
    public void deSerialize() {
        String json = "{\"author\":\"" + AUTHOR +"\",\"message\":\"" + MESSAGE + "\"}";
        NewsItem deserializedNewsItem = gson.fromJson(json, NewsItem.class);
        Assert.assertEquals(AUTHOR, deserializedNewsItem.getAuthor());
        Assert.assertEquals(MESSAGE, deserializedNewsItem.getMessage());
    }
}
