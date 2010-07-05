package com.sincerial.news.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: khellan
 * Date: Jun 29, 2010
 * Time: 1:20:08 PM
 * Servlet invalidating the session for the user.
 */
public class SignOut extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        // TODO: Do some authorization rather than just accepting

        HttpSession session = request.getSession();
        session.invalidate();
        Logger logger = Logger.getLogger(SignOut.class.getPackage().getName());
        logger.info("{\"signed_out\":true}");
        out.println("{\"signed_out\":true}");
    }
}