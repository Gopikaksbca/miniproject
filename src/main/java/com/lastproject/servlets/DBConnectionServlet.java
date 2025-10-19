package com.lastproject.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/connectDB")
public class DBConnectionServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection con = DatabaseConnection.getConnection()) {
            if(con != null) {
                out.println("<h2>✅ Database Connected Successfully!</h2>");
            } else {
                out.println("<h2>❌ Database Connection Failed!</h2>");
            }
        } catch (Exception e) {
            out.println("<h2>❌ Database Connection Failed!</h2>");
            e.printStackTrace(out);
        }
    }
}
