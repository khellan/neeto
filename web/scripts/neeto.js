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
        com.sincerial.news.request_feed();
    }
)

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

com.sincerial.news.show_feed = function(items) {
    var html = "";
    for (var i in items) {
        var item = items[i];
        html += "<li>";
        html += "<button id='" + item.document_id + "' type='button'>Like</button>"
        html += "<div class='author' id='author_" + item.document_id + "'>" + item.author + "</div>";
        html += "<div class='message' id='message_" + item.document_id + "'>" + item.message + "</div>";
        html += "</li>";
    }
    $("#feed_items").html(html);
    for (var i in items) {
        var item = items[i];
        $("#" + item.document_id).click(
            function() {
                com.sincerial.news.submit_like(this.id);
            }        
        );
    }
}

com.sincerial.news.submit_like_button = function(document_id, button_text, click_function, like) {
    var message = $("#message_" + document_id).html();
    var author = $("#author_" + document_id).html();

    $.post(
        com.sincerial.news.LIKE_URL,
        {
            'vendor_id': com.sincerial.news.VENDOR_ID,
            'user_id': '',
            "document_id": document_id,
            "category": com.sincerial.news.CATEGORY,
            "message": message,
            "author": author,
            "like": like
        },
        function(response) {
            $("#" + document_id).html(button_text);
            $("#" + document_id).unbind('click');
            $("#" + document_id).click(
                function() {
                    click_function(this.id);
                }
            )
        }
    );
}

com.sincerial.news.submit_like = function(document_id) {
    com.sincerial.news.submit_like_button(document_id, "Unlike", com.sincerial.news.submit_unlike, true);
}

com.sincerial.news.submit_unlike = function(document_id) {
    com.sincerial.news.submit_like_button(document_id, "Like", com.sincerial.news.submit_like, false);
}
