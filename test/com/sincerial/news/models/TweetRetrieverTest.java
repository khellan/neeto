package com.sincerial.news.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 28, 2010
 * Time: 10:33:10 AM
 * Tests the TweetRetriever class. No tests of the actual twitter communication, but that is actually handled by another class anyway
 */
public class TweetRetrieverTest {
    public static final String TWITTER_BASE = "http://twitter.com/";
    public static final String TWITTER_HASH_SEARCH = "http://twitter.com/#search?q=%23";

    @Before
    public void setUp() throws MalformedURLException {
    }

    @Test
    public void testExtractHyperlinks() throws MalformedURLException {
        String message = "@balle:man http://bit.ly/87asdf rocks. RT @c3po #fail and@foo_bar_#rofl";
        Map<String, URL> expectedHyperlinks = new HashMap<String, URL>();
        expectedHyperlinks.put("@balle", new URL(TWITTER_BASE + "balle"));
        expectedHyperlinks.put("http://bit.ly/87asdf", new URL("http://bit.ly/87asdf"));
        expectedHyperlinks.put("@c3po", new URL(TWITTER_BASE + "c3po"));
        expectedHyperlinks.put("@foo_bar_", new URL(TWITTER_BASE + "foo_bar_"));
        expectedHyperlinks.put("#fail", new URL(TWITTER_HASH_SEARCH + "fail"));
        expectedHyperlinks.put("#rofl", new URL(TWITTER_HASH_SEARCH + "rofl"));
        Map<String, URL> actualHyperlinks = TweetRetriever.extractHyperlinks(message);
        Assert.assertEquals(expectedHyperlinks, actualHyperlinks);
    }
}
