package com.sincerial.news.models;

/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 14, 2010
 * Time: 10:32:55 AM
 */

/**
 * Exception signaling communication error with news source. It's main reason for being is to avoid cluttering code with
 * source specific code
 */
public class RetrievalException extends Exception {
    /**
     * Constructs an exception
     * @param message The error message
     * @param cause The original exception cause
     */
    public RetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}
