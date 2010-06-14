/**
 *
 */

var com;
if (!com) com = {};
if (!com.sincerial) com.sincerial = {};
if (!com.sincerial.news) com.sincerial.news = {};

com.sincerial.news.FEED_URL = 'feed';

$(document).ready(
    function(event) {
        com.sincerial.news.request_feed();
    }
)

com.sincerial.news.request_feed = function() {
    $.getJSON(
        com.sincerial.news.FEED_URL,
        {},
        function(news_items) {
            com.sincerial.news.show_feed(news_items);
        }
    );
}

com.sincerial.news.show_feed = function(items) {
    var html = "";
    for (var i = 0; i < items.length; ++i) {
        var item = items[i];
        html += "<li>";
        html += "<div class='author'>" + item.author + "</div>";
        html += "<div class='message'>" + item.message + "</div>";
        html += "</li>";
    }
    $("#feed_items").html(html);
}