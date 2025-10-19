package com.lastproject.servlets;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/OverseasServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,   // 2MB
                 maxFileSize = 1024 * 1024 * 10,        // 10MB
                 maxRequestSize = 1024 * 1024 * 50)     // 50MB
public class OverseasServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String passportNumber = request.getParameter("passportNumber");
        String dob = request.getParameter("dob");
        String country = request.getParameter("country");

        Part passportFile = request.getPart("passportFile");
        Part aadhaarFile = request.getPart("aadhaarFile");

        // Upload folder
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        String passportFileName = passportFile.getSubmittedFileName();
        String aadhaarFileName = (aadhaarFile != null && aadhaarFile.getSize() > 0) ? aadhaarFile.getSubmittedFileName() : "";

        if(passportFileName != null && !passportFileName.isEmpty())
            passportFile.write(uploadPath + File.separator + passportFileName);
        if(aadhaarFileName != null && !aadhaarFileName.isEmpty())
            aadhaarFile.write(uploadPath + File.separator + aadhaarFileName);

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM overseas_voters WHERE passport_number=? AND date_of_birth=? AND country=?"
            );
            ps.setString(1, passportNumber);
            ps.setString(2, dob);
            ps.setString(3, country);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Login success → Update uploaded file paths
                PreparedStatement psUpdate = con.prepareStatement(
                    "UPDATE overseas_voters SET passport_file=?, aadhaar_file=? WHERE passport_number=?"
                );
                psUpdate.setString(1, "uploads/" + passportFileName);
                psUpdate.setString(2, aadhaarFileName.isEmpty() ? null : "uploads/" + aadhaarFileName);
                psUpdate.setString(3, passportNumber);
                psUpdate.executeUpdate();

                response.sendRedirect("ballot.html"); // Redirect to voting page
            } else {
                out.println("<h3>Login Failed! Check your Passport Number, DOB, or Country.</h3>");
            }

            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
