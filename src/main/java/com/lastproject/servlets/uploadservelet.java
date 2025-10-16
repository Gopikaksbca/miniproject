package com.lastproject.servlets;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10,      // 10MB
                 maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class uploadservelet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String dob = request.getParameter("dob");
        String passport_id = request.getParameter("passport_id");

        Part passportPart = request.getPart("passport_file");
        Part voterPart = request.getPart("voter_id_file");

        // Folder to save uploads
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        // Save files
        String passportFileName = passportPart.getSubmittedFileName();
        String voterFileName = voterPart.getSubmittedFileName();

        passportPart.write(uploadPath + File.separator + passportFileName);
        voterPart.write(uploadPath + File.separator + voterFileName);

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO users (name, dob, passport_id, user_type, passport_file, voter_id_file, is_verified) VALUES (?, ?, ?, 'overseas', ?, ?, false)"
            );
            ps.setString(1, name);
            ps.setString(2, dob);
            ps.setString(3, passport_id);
            ps.setString(4, "uploads/" + passportFileName);
            ps.setString(5, "uploads/" + voterFileName);
            ps.executeUpdate();

            response.getWriter().println("✅ Uploaded successfully and saved to database!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
