/**
 *
 */

var com;
if (!com) com = {};
if (!com.sincerial) com.sincerial = {};
if (!com.sincerial.news) com.sincerial.news = {};

com.sincerial.news.FEED_URL = 'feed';
com.sincerial.news.LIKE_URL = 'like';
com.sincerial.news.SIGN_IN_URL = 'sign_in';
com.sincerial.news.SIGN_OUT_URL = 'sign_out';
com.sincerial.news.VENDOR_ID = '301';
com.sincerial.news.CATEGORY = 'news';

$(document).ready(
    function(event) {
        com.sincerial.news.request_feed();
    }
)


com.sincerial.news.sign_in_element_id;
com.sincerial.news.sign_in_area_id;
com.sincerial.news.sign_in_form_id;
com.sincerial.news.sign_in_button_id;
com.sincerial.news.initialize_sign_in = function(element_id, sign_in_area_id, sign_in_form_id, sign_in_button_id) {
    com.sincerial.news.sign_in_element_id = element_id;
    com.sincerial.news.sign_in_area_id = sign_in_area_id;
    com.sincerial.news.sign_in_form_id = sign_in_form_id;
    com.sincerial.news.sign_in_button_id = sign_in_button_id;
    $(element_id).click(
        function() {
            com.sincerial.news.sign_in();
        }
    );
    $(com.sincerial.news.sign_in_area_id).hide();    
}

com.sincerial.news.sign_out_element_id;
com.sincerial.news.initialize_sign_out = function(element_id) {
    com.sincerial.news.sign_out_element_id = element_id;
    $(element_id).click(
        function() {
            com.sincerial.news.sign_out();
        }
    );
}

com.sincerial.news.sign_in = function() {
    $(com.sincerial.news.sign_in_button_id).unbind("click")
    $(com.sincerial.news.sign_in_form_id).submit(
        function() {
            com.sincerial.news.submit_sign_in();
        }
    )
    $(com.sincerial.news.sign_in_button_id).click(
        function() {
            com.sincerial.news.submit_sign_in();
        }
    )
    $(com.sincerial.news.sign_in_area_id).show();
}

com.sincerial.news.submit_sign_in = function() {
    $.post(
        com.sincerial.news.SIGN_IN_URL,
        {
            "user_id": $("#sign_in_user_id").val(),
            "password": $("#sign_in_password").val()
        },
        function(response) {
            $(com.sincerial.news.sign_in_area_id).hide();
            com.sincerial.news.signed_in($("#sign_in_user_id").val());
            com.sincerial.news.request_feed();
        }
    )
}

com.sincerial.news.sign_out = function() {
    $.post(
        com.sincerial.news.SIGN_OUT_URL,
        {},
        function(response) {
            com.sincerial.news.signed_out();
            com.sincerial.news.request_feed();
        }
    );
}

com.sincerial.news.signed_in = function(user_id) {
    $(com.sincerial.news.sign_in_element_id).hide();
    $(com.sincerial.news.sign_out_element_id).show();
    $("#signed_in_user_id").html(user_id + " | ");
}

com.sincerial.news.signed_out = function() {
    $(com.sincerial.news.sign_out_element_id).hide();
    $(com.sincerial.news.sign_in_element_id).show();
}

com.sincerial.news.request_feed = function() {
    $.getJSON(
        com.sincerial.news.FEED_URL,
        {
            'vendor_id': com.sincerial.news.VENDOR_ID,
        },
        function(news_item_package) {
            com.sincerial.news.show_feed(news_item_package);
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

com.sincerial.news.show_feed = function(news_item_package) {
    var html = "";
    var signed_in = news_item_package.signed_in;
    var items = news_item_package.items; 
    for (var i in items) {
        var item = items[i];
        html += "<li>";
        html += "<img src='images/twitter_t.png'>";
        if (signed_in) {
            html += "<button id='" + item.product_id + "' type='button'>Like</button>"
        }
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
