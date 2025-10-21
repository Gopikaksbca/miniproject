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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String voterNumber = request.getParameter("voterNumber");
        String dob = request.getParameter("dob");

        if (voterNumber == null || voterNumber.isEmpty() || dob == null || dob.isEmpty()) {
            out.println("<h3>❌ Please provide Voter ID and Date of Birth.</h3>");
            return;
        }

        Part voterFile = null, aadhaarFile = null;
        String voterFileName = "", aadhaarFileName = "";

        try {
            voterFile = request.getPart("voterFile");
            aadhaarFile = request.getPart("aadhaarFile");

            if (voterFile != null && voterFile.getSize() > 0) {
                voterFileName = new File(voterFile.getSubmittedFileName()).getName();
            }
            if (aadhaarFile != null && aadhaarFile.getSize() > 0) {
                aadhaarFileName = new File(aadhaarFile.getSubmittedFileName()).getName();
            }

            // Upload folder
            String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();

            if (!voterFileName.isEmpty()) voterFile.write(uploadPath + File.separator + voterFileName);
            if (!aadhaarFileName.isEmpty()) aadhaarFile.write(uploadPath + File.separator + aadhaarFileName);

        } catch (Exception e) {
            out.println("<h3>❌ Error uploading files.</h3>");
            e.printStackTrace(out);
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // ✅ Use the correct DBConnection method
            con = DBConnection.getConnection();

            if (con == null) {
                out.println("<h3>❌ Database connection failed!</h3>");
                return;
            }

            String sql = "SELECT * FROM domestic_voters WHERE voter_number=? AND date_of_birth=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, voterNumber);
            ps.setString(2, dob); // make sure DOB format matches DB (YYYY-MM-DD)
            rs = ps.executeQuery();

            if (rs.next()) {
                String updateSQL = "UPDATE domestic_voters SET voter_file=?, aadhaar_file=? WHERE voter_number=?";
                PreparedStatement psUpdate = con.prepareStatement(updateSQL);
                psUpdate.setString(1, voterFileName.isEmpty() ? null : "uploads/" + voterFileName);
                psUpdate.setString(2, aadhaarFileName.isEmpty() ? null : "uploads/" + aadhaarFileName);
                psUpdate.setString(3, voterNumber);
                psUpdate.executeUpdate();
                psUpdate.close();

                response.sendRedirect("ballot.html");
            } else {
                out.println("<h3>❌ Login Failed! Check your Voter ID or Date of Birth.</h3>");
            }

        } catch (Exception e) {
            out.println("<h3>❌ Error during login. Check server logs.</h3>");
            e.printStackTrace(out);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
            try { if (con != null) con.close(); } catch (SQLException ignored) {}
        }
    }
}
