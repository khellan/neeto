package com.sincerial.news.model;

/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 11, 2010
 * Time: 1:30:48 PM
 */

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterException;

/**
 *  Handles communication with the Twitter
 */
public class TweetRetriever implements NewsRetriever {
    public static final String CATEGORY = "news";
    public static final String TWITTER_BASE = "http://twitter.com/";
    public static final String TWITTER_HASH_SEARCH = "http://twitter.com/#search?q=%23";

    /**
     * Interface for a faimily of timeline getters. Basically a poor-mans functional programming approach
     */
    protected interface TimelineGetter {
        /**
         * Retrieves the timeline
         * @return A {@link List}<{@link Status}> of Status messages
         * @throws TwitterException if there are problems
         */
        public List<Status> getTimeline() throws TwitterException;
    }

    /**
     * TimelineGetter to retrieve the public timeline
     */
    protected class PublicTimelineGetter implements TimelineGetter {
        protected Twitter twitter;

        PublicTimelineGetter(Twitter twitter) {
            this.twitter = twitter;
        }

        public List<Status> getTimeline() throws TwitterException {
            return twitter.getPublicTimeline();
        }
    }

    /**
     * TimelineGetter to retrieve the the user's friends' merged timelines
     */
    protected class FriendsTimelineGetter implements TimelineGetter {
        protected Twitter twitter;

        FriendsTimelineGetter(Twitter twitter) {
            this.twitter = twitter;
        }

        public List<Status> getTimeline() throws TwitterException {
            return twitter.getFriendsTimeline();
        }
    }

    protected static Map<String, String> extractMentions(String message, Map<String, String> hyperlinks) {
        Pattern extractMentions = Pattern.compile("@[\\w]{1,20}");

        Matcher mentions = extractMentions.matcher(message);
        while (mentions.find()) {
            String mention = mentions.group();
            hyperlinks.put(mention, TWITTER_BASE + mention.substring(1));
        }

        return hyperlinks;
    }

    protected static Map<String, String> extractHashtags(String message, Map<String, String> hyperlinks) {
        Pattern extractHashtags = Pattern.compile("(?<!&)(#[\\w\\xc0-\\xd6\\xd8-\\xf6\\xf8-\\xff0-9_]+)");

        Matcher hashtags = extractHashtags.matcher(message);
        while (hashtags.find()) {
            String hashtag = hashtags.group();
            hyperlinks.put(hashtag, TWITTER_HASH_SEARCH + hashtag.substring(1));
        }

        return hyperlinks;
    }

    protected static Map<String, String> extractURLs(String message, Map<String, String> hyperlinks) {
        Pattern extractURLs = Pattern.compile("(https?:\\/\\/|www\\.)[\\S]++");

        Matcher URLs = extractURLs.matcher(message);
        while (URLs.find()) {
            String url = URLs.group();
            hyperlinks.put(url, url);
        }

        return hyperlinks;
    }

    protected static Map<String, String> extractHyperlinks(String message) {
        Map<String, String> hyperlinks = new HashMap<String, String>();

        extractMentions(message, hyperlinks);
        extractHashtags(message, hyperlinks);
        extractURLs(message, hyperlinks);

        return hyperlinks;
    }

    /**
     * Parses the twitter {@link Status} message into a {@link NewsItem}
     * @param status The twitter {@link Status} message to parse
     * @return The {@link NewsItem} with the parsed message
     */
    protected NewsItem parseTweet(Status status) {
        return new NewsItem(Long.toString(
                status.getId()), CATEGORY, status.getUser().getName(),
                status.getCreatedAt().getTime() / 1000,
                status.getText(), extractHyperlinks(status.getText()));
    }

    /**
     * Retreives the timeline specified by the {@link TimelineGetter}
     * @param getter The {@link TimelineGetter} to use for retrieving messages
     * @return public timeline as a {@link List}<{@link NewsItem}>
     * @throws RetrievalException on errors communicating with the news source
     */
    protected List<NewsItem> getTimeline(TimelineGetter getter) throws RetrievalException {
        ArrayList<NewsItem> messages = new ArrayList<NewsItem>();

        try {
            for(Status status: getter.getTimeline()) {
                messages.add(parseTweet(status));
            }
        } catch(TwitterException e) {
            throw new RetrievalException("Tweet retrieval failed", e);
        }

        return messages;
    }

    public List<NewsItem> getPublicTimeline() throws RetrievalException {
        return getTimeline(new PublicTimelineGetter(new TwitterFactory().getInstance()));
    }

    public List<NewsItem> getUserTimeline(String userId, String password) throws RetrievalException {
        return getTimeline(new FriendsTimelineGetter(new TwitterFactory().getInstance(userId, password)));
    }
}
