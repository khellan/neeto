package com.sincerial.news.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
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
    public void testExtractHyperlinks() {
        String message = "@balle:man http://bit.ly/87asdf rocks. RT @c3po #fail and@foo_bar_#rofl";
        Map<String, String> expectedHyperlinks = new HashMap<String, String>();
        expectedHyperlinks.put("@balle", TWITTER_BASE + "balle");
        expectedHyperlinks.put("http://bit.ly/87asdf", "http://bit.ly/87asdf");
        expectedHyperlinks.put("@c3po", TWITTER_BASE + "c3po");
        expectedHyperlinks.put("@foo_bar_", TWITTER_BASE + "foo_bar_");
        expectedHyperlinks.put("#fail", TWITTER_HASH_SEARCH + "fail");
        expectedHyperlinks.put("#rofl", TWITTER_HASH_SEARCH + "rofl");
        Map<String, String> actualHyperlinks = TweetRetriever.extractHyperlinks(message);
        Assert.assertEquals(expectedHyperlinks.size(), actualHyperlinks.size());
        Assert.assertEquals(expectedHyperlinks, actualHyperlinks);
    }

    @Test
    public void testExtractHyperlinksNotExtractingEntities() {
        String message = "RT @philsherry Just wrote about last night&#39;s @SuperMondays: SuperFreelancers: " +
                "http://philsherry.com/305 #NEfollowers";
        Map<String, String> expectedHyperlinks = new HashMap<String, String>();
        expectedHyperlinks.put("@philsherry", TWITTER_BASE + "philsherry");
        expectedHyperlinks.put("@SuperMondays", TWITTER_BASE + "SuperMondays");
        expectedHyperlinks.put("http://philsherry.com/305", "http://philsherry.com/305");
        expectedHyperlinks.put("#NEfollowers", TWITTER_HASH_SEARCH + "NEfollowers");
        Map<String, String> actualHyperlinks = TweetRetriever.extractHyperlinks(message);
        Assert.assertEquals(expectedHyperlinks.size(), actualHyperlinks.size());
        Assert.assertEquals(expectedHyperlinks, actualHyperlinks);        
    }

    @Test
    public void testExtractHyperlinksNotSkippingStartHashTag() {
        String message = "#39;s @SuperMondays: SuperFreelancers: ";
        Map<String, String> expectedHyperlinks = new HashMap<String, String>();
        expectedHyperlinks.put("#39", TWITTER_HASH_SEARCH + "39");
        expectedHyperlinks.put("@SuperMondays", TWITTER_BASE + "SuperMondays");
        Map<String, String> actualHyperlinks = TweetRetriever.extractHyperlinks(message);
        Assert.assertEquals(expectedHyperlinks.size(), actualHyperlinks.size());
        Assert.assertEquals(expectedHyperlinks, actualHyperlinks);
    }
}
