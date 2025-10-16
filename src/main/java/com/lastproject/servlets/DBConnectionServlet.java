package com.lastproject.servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/connectDB")
public class DBConnectionServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection con = DatabaseConnection.getConnection()) {
            out.println("<h2>✅ Database Connected Successfully!</h2>");
        } catch (Exception e) {
            out.println("<h2>❌ Database Connection Failed!</h2>");
            e.printStackTrace(out);
        }
    }
}
