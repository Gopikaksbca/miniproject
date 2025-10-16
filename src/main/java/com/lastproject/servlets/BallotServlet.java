package com.lastproject.servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/BallotServlet")
public class BallotServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        String candidate = request.getParameter("candidate");
        String voterId = request.getParameter("voterId");
        String voterType = request.getParameter("voterType"); // "domestic" or "overseas"

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // ✅ Include your DB connection here
            con = DatabaseConnection.getConnection(); 

            // 1️⃣ Check if voter has already voted
            String voterTable = voterType.equalsIgnoreCase("domestic") ? "domestic_voters" : "overseas_voters";
            ps = con.prepareStatement("SELECT has_voted FROM " + voterTable + " WHERE " + 
                                      (voterType.equalsIgnoreCase("domestic") ? "voter_number" : "passport_number") + "=?");
            ps.setString(1, voterId);
            rs = ps.executeQuery();

            if (rs.next()) {
                boolean hasVoted = rs.getBoolean("has_voted");
                if (hasVoted) {
                    out.println("⚠️ You have already voted. Voting is allowed only once.");
                    return;
                }
            } else {
                out.println("⚠️ Voter not found. Check your Voter ID / Passport Number.");
                return;
            }

            // 2️⃣ Update candidate votes
            ps = con.prepareStatement("UPDATE candidates SET votes = votes + 1 WHERE name = ?");
            ps.setString(1, candidate);
            int updated = ps.executeUpdate();
            if (updated == 0) {
                out.println("⚠️ Candidate not found. Vote not counted.");
                return;
            }

            // 3️⃣ Mark voter as has_voted
            ps = con.prepareStatement("UPDATE " + voterTable + " SET has_voted = true WHERE " + 
                                      (voterType.equalsIgnoreCase("domestic") ? "voter_number" : "passport_number") + "=?");
            ps.setString(1, voterId);
            ps.executeUpdate();

            out.println("✅ Vote submitted successfully! Thank you for voting.");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("⚠️ Error processing vote. Try again.");
        } finally {
            try { if (rs != null) rs.close(); } catch(Exception e) {}
            try { if (ps != null) ps.close(); } catch(Exception e) {}
            try { if (con != null) con.close(); } catch(Exception e) {}
        }
    }
}
