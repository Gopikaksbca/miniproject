package com.lastproject.servlets;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;
import db.DBConnection;

@WebServlet("/test")
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            out.println("<h2>✅ Database Connected Successfully</h2>");
        } catch (Exception e) {
            out.println("<h2>❌ Database Connection Failed</h2>");
            e.printStackTrace(out);
        }
    }
}
