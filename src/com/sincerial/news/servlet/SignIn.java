package com.sincerial.news.servlet;

import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 29, 2010
 * Time: 1:20:08 PM
 * Servlet initializing a session for the user.
 */
public class SignIn extends HttpServlet {
    public static final String USER_ID = "user_id";
    public static final String PASSWORD = "password";

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; Charset=UTF-8");
        PrintWriter out = response.getWriter();

        String userId = request.getParameter(USER_ID);
        String password = request.getParameter(PASSWORD);

        // TODO: Do some authorization rather than just accepting
        
        HttpSession session = request.getSession();
        session.setAttribute(USER_ID, userId);
        session.setAttribute(PASSWORD, password);

        Logger logger = Logger.getLogger(SignOut.class.getPackage().getName());
        logger.info("{\"signed_in\":\"" + userId + "\"}");
        out.println("{\"signed_in\":\"" + userId + "\"}");
    }
}
