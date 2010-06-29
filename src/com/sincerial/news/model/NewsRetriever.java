package com.sincerial.news.model;

/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 14, 2010
 * Time: 10:28:50 AM
 */

import java.util.List;

/**
 * Interface for news retrievers. This interface should be used for all news sources, but it might have to be modified along the way.
 */
public interface NewsRetriever {
    /**
     * Retrieves the public timeline as {@link NewsItem}s
     *
     * @return public timeline as a {@link List}<{@link NewsItem}>
     * @throws RetrievalException on errors communicating with the news source
     */
    public List<NewsItem> getPublicTimeline() throws RetrievalException;

    /**
     * Retrieves the timeline of a specific user
     * 
     * @param user The username of to get timeline for
     * @param password The password of the user
     * @return The user's timeline as a {@link List}<{@link NewsItem}>
     * @throws RetrievalException on errors communicating with the news source
     */
    public List<NewsItem> getUserTimeline(String user, String password) throws RetrievalException;
}
