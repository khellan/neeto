/**
 *
 */

var com;
if (!com) com = {};
if (!com.sincerial) com.sincerial = {};
if (!com.sincerial.news) com.sincerial.news = {};

com.sincerial.news.FEED_URL = 'feed';
com.sincerial.news.LIKE_URL = 'like';
com.sincerial.news.VENDOR_ID = '301';
com.sincerial.news.CATEGORY = 'news';

$(document).ready(
    function(event) {
        com.sincerial.news.login();
        com.sincerial.news.request_feed();
    }
)

com.sincerial.news.login = function() {
    $("#sign_in").html('Sign out');
}

com.sincerial.news.request_feed = function() {
    $.getJSON(
        com.sincerial.news.FEED_URL,
        {
            'vendor_id': com.sincerial.news.VENDOR_ID,
            'user_id': '',
            'password': ''
        },
        function(news_items) {
            com.sincerial.news.show_feed(news_items);
        }
    );
}

com.sincerial.news.format_field = function(message, matches, hyperlinks) {
    var result = message;
    for (i in matches) {
        var field = matches[i];
        if (field in hyperlinks) {
            result = result.replace(field, '<a href="' + hyperlinks[field] + '">' + field + "</a>");
        }
    }
    return result;
}
com.sincerial.news.format_mentions = function(message, hyperlinks) {
    return com.sincerial.news.format_field(message, message.match(/@[\w]{1,20}/g), hyperlinks);
}

com.sincerial.news.format_hashtags = function(message, hyperlinks) {
    return com.sincerial.news.format_field(message, message.match(/#[\w\xc0-\xd6\xd8-\xf6\xf8-\xff0-9_]+/g), hyperlinks);
}

com.sincerial.news.format_urls = function(message, hyperlinks) {
    return com.sincerial.news.format_field(message, message.match(/(https?:\/\/|www\.)[\S]+/g), hyperlinks);
}

com.sincerial.news.format_message = function(message, hyperlinks) {
    var result = message;
    result = com.sincerial.news.format_mentions(result, hyperlinks);
    result = com.sincerial.news.format_hashtags(result, hyperlinks);
    result = com.sincerial.news.format_urls(result, hyperlinks);
    return result;
}

com.sincerial.news.show_feed = function(items) {
    var html = "";
    for (var i in items) {
        var item = items[i];
        html += "<li>";
        html += "<img src='images/twitter_t.png'>";
        html += "<button id='" + item.product_id + "' type='button'>Like</button>"
        html += "<div class='author' id='author_" + item.product_id + "'>" + item.author + "</div>";
        html += "<div class='message' id='message_" + item.product_id + "'>" +
            com.sincerial.news.format_message(item.message, item.hyperlinks) + "</div>";
        html += "</li>";
    }
    $("#feed_items").html(html);
    for (var i in items) {
        var item = items[i];
        $("#" + item.product_id).click(
            function() {
                com.sincerial.news.submit_like(this.id);
            }        
        );
    }
}

com.sincerial.news.submit_like_button = function(product_id, button_text, click_function, like) {
    var message = $("#message_" + product_id).html();
    var author = $("#author_" + product_id).html();

    $.post(
        com.sincerial.news.LIKE_URL,
        {
            'vendor_id': com.sincerial.news.VENDOR_ID,
            'user_id': '',
            "product_id": product_id,
            "category": com.sincerial.news.CATEGORY,
            "message": message,
            "author": author,
            "like": like
        },
        function(response) {
            $("#" + product_id).html(button_text);
            $("#" + product_id).unbind('click');
            $("#" + product_id).click(
                function() {
                    click_function(this.id);
                }
            )
        }
    );
}

com.sincerial.news.submit_like = function(product_id) {
    com.sincerial.news.submit_like_button(product_id, "Unlike", com.sincerial.news.submit_unlike, true);
}

com.sincerial.news.submit_unlike = function(product_id) {
    com.sincerial.news.submit_like_button(product_id, "Like", com.sincerial.news.submit_like, false);
}
