package com.sincerial.news.models;

/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 11, 2010
 * Time: 4:05:06 PM
 */

/**
 * General class to store news items to avoid cluttering code with source specific stuff
 */
public class NewsItem {
    String author;
    String message;

    /**
     * Constructs a NewsItem
     *
     * @param author The author of this message or article if known
     * @param message The actual message or article
     */
    public NewsItem(String author, String message) {
        this.author = author;
        this.message = message;
    }

    /**
     * @return A {@link String} with the author
     */
    public String getAuthor() {return author;}

    /**
     * @return A {@link String} with the message
     */
    public String getMessage() {return message;}
}
