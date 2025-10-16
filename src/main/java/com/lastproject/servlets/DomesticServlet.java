package com.lastproject.servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/DomesticServlet")
public class DomesticServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String voterNumber = request.getParameter("voterNumber");
        String dob = request.getParameter("dob");

        Part voterFile = request.getPart("voterFile");
        Part aadhaarFile = request.getPart("aadhaarFile");

        // Upload folder
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        // Save files
        String voterFileName = voterFile.getSubmittedFileName();
        String aadhaarFileName = aadhaarFile.getSubmittedFileName();
        voterFile.write(uploadPath + File.separator + voterFileName);
        aadhaarFile.write(uploadPath + File.separator + aadhaarFileName);

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM domestic_voters WHERE voter_number=? AND date_of_birth=?"
            );
            ps.setString(1, voterNumber);
            ps.setString(2, dob);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Login successful → Update uploaded files paths
                PreparedStatement psUpdate = con.prepareStatement(
                    "UPDATE domestic_voters SET voter_file=?, aadhaar_file=? WHERE voter_number=?"
                );
                psUpdate.setString(1, "uploads/" + voterFileName);
                psUpdate.setString(2, "uploads/" + aadhaarFileName);
                psUpdate.setString(3, voterNumber);
                psUpdate.executeUpdate();

                response.sendRedirect("ballot.html"); // Redirect to voting page
            } else {
                response.getWriter().println("<h3>Login Failed! Check your Voter ID or DOB.</h3>");
            }

            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace(response.getWriter());
        }
    }
}
