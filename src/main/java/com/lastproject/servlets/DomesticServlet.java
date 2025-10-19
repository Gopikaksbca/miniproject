package com.lastproject.servlets;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/DomesticServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,   // 2MB
                 maxFileSize = 1024 * 1024 * 10,        // 10MB
                 maxRequestSize = 1024 * 1024 * 50)     // 50MB
public class DomesticServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String voterNumber = request.getParameter("voterNumber");
        String dob = request.getParameter("dob");

        Part voterFile = request.getPart("voterFile");
        Part aadhaarFile = request.getPart("aadhaarFile");

        // Upload folder
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        String voterFileName = voterFile.getSubmittedFileName();
        String aadhaarFileName = aadhaarFile.getSubmittedFileName();

        voterFile.write(uploadPath + File.separator + voterFileName);
        aadhaarFile.write(uploadPath + File.separator + aadhaarFileName);

        try {
            Connection con = DatabaseConnection.getConnection(); // Your DB connection class
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM domestic_voters WHERE voter_number=? AND date_of_birth=?"
            );
            ps.setString(1, voterNumber);
            ps.setString(2, dob);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Login success → Update uploaded file paths
                PreparedStatement psUpdate = con.prepareStatement(
                    "UPDATE domestic_voters SET voter_file=?, aadhaar_file=? WHERE voter_number=?"
                );
                psUpdate.setString(1, "uploads/" + voterFileName);
                psUpdate.setString(2, "uploads/" + aadhaarFileName);
                psUpdate.setString(3, voterNumber);
                psUpdate.executeUpdate();

                response.sendRedirect("ballot.html"); // Redirect to voting page
            } else {
                out.println("<h3>Login Failed! Check your Voter ID or Date of Birth.</h3>");
            }

            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
