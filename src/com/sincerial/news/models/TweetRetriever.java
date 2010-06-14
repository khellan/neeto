package com.sincerial.news.models;

/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 11, 2010
 * Time: 1:30:48 PM
 */

import java.util.List;
import java.util.ArrayList;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterException;

/**
 *  Handles communication with the Twitter
 */
public class TweetRetriever implements NewsRetriever {
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

    /**
     * Parses the twitter {@link Status} message into a {@link NewsItem}
     * @param status The twitter {@link Status} message to parse
     * @return The {@link NewsItem} with the parsed message
     */
    protected NewsItem parseTweet(Status status) {
        return new NewsItem(status.getUser().getName(), status.getText());
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
            throw new RetrievalException("Tweet retrieval failed", e.getCause());
        }

        return messages;
    }

    public List<NewsItem> getPublicTimeline() throws RetrievalException {
        return getTimeline(new PublicTimelineGetter(new TwitterFactory().getInstance()));
    }

    public List<NewsItem> getUserTimeline(String user, String password) throws RetrievalException {
        return getTimeline(new FriendsTimelineGetter(new TwitterFactory().getInstance(user, password)));
    }
}
