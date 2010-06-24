package com.sincerial.news.models;

import com.google.gson.annotations.SerializedName;

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
    @SerializedName("product_id") String id;
    String category;
    String author;
    String message;

    /**
     * No-args constructor for Gson deserialization
     */
    public NewsItem() {}
    
    /**
     * Constructs a NewsItem
     *
     * @param id The id of this message
     * @param author The author of this message or article if known
     * @param message The actual message or article
     */
    public NewsItem(String id, String category, String author, String message) {
        this.id = id;
        this.category = category;
        this.author = author;
        this.message = message;
    }

    /**
     * @return A {@link String} with the id
     */
    public String getId() {return id;}

    /**
     * @return A {@link String} with the id
     */
    public String getCategory() {return category;}

    /**
     * @return A {@link String} with the author
     */
    public String getAuthor() {return author;}

    /**
     * @return A {@link String} with the message
     */
    public String getMessage() {return message;}
}
