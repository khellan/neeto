package com.sincerial.news.models;

import com.google.gson.annotations.SerializedName;

import java.net.URL;
import java.util.Collections;
import java.util.Map;

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
    Map<String, String> hyperlinks;

    /**
     * No-args constructor for Gson deserialization
     */
    public NewsItem() {}
    
    /**
     * Constructs a NewsItem without hyperlinks
     *
     * @param id The id of this message
     * @param category The category of this message
     * @param author The author of this message or article if known
     * @param message The actual message or article
     */
    public NewsItem(String id, String category, String author, String message) {
        this(id, category, author, message, Collections.<String, String>emptyMap());
    }

    /**
     * Constructs a NewsItem
     *
     * @param id The id of this message
     * @param category The category of this message
     * @param author The author of this message or article if known
     * @param message The actual message or article
     * @param hyperlinks A {@link Map}<{@link String}, {@link URL}> mapping anchor text to hyperlink
     */
    public NewsItem(String id, String category, String author, String message, Map<String, String> hyperlinks) {
        this.id = id;
        this.category = category;
        this.author = author;
        this.message = message;
        this.hyperlinks = hyperlinks;
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

    /**
     * @return A {@link Map}<{@link String}, {@link URL}> mapping anchor text to hyperlink
     */
    public Map<String, String> getHyperlinks() {return hyperlinks;}
}
