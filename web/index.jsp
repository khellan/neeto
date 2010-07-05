<!DOCTYPE HTML>
<html>
<head>
    <title>Neeto</title>
    <script type="text/javascript" src="scripts/jquery.js"></script>
    <script type="text/javascript" src="scripts/neeto.js"></script>
    <link rel="stylesheet" href="styles/neeto.css" type="text/css" />
    <meta charset="utf-8">
</head>

<body>
    <ul class="menu">
        <li>
            <a href="#" id="sign_in">Sign in</a>
            <a href="#" id="sign_out">Sign out</a>
            <div id="sign_in_area">
                <form id="sign_in_form" method="post">
                    <div class="label">Username</div>
                    <input type="text" name="user_id" id="sign_in_user_id">
                    <div class="password">Password</div>
                    <input type="password" name="password" id="sign_in_password">
                    <button id="sign_in_button" type="button">Sign in</button>
                </form>
            </div>
            <script type="text/javascript">
                com.sincerial.news.initialize_sign_in("#sign_in", "#sign_in_area", "#sign_in_form", "#sign_in_button");
                com.sincerial.news.initialize_sign_out("#sign_out");
                <% if (session.getAttribute("user_id") == null) { %>
                    com.sincerial.news.signed_out();
                <% } else { %>
                    com.sincerial.news.signed_in();
                <% } %>
            </script>
        </li>
    </ul>

    <header>
        <h1>Neeto</h1>
        <h2>Tweets for you</h2>
    </header>

    <section>
        <ul id="feed_items">
            <li>Retrieving your tweets</li>
        </ul>
    </section>

    <footer>
        (c) 2010 Sincerial AS
    </footer>
</body>
</html>