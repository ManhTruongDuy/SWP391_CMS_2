package controller;

import com.google.gson.Gson;
import dao.AdminDAO;
import jakarta.servlet.ServletException;
import model.*;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.accounts.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/admin")
public class AdminServlet extends HttpServlet {
    private final AdminDAO dao = new AdminDAO();
    private final Gson gson = new Gson();

    //Handles CORS preflight requests
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try (PrintWriter out = response.getWriter()) {
            switch (action) {
                case "pharmacists" -> out.print(gson.toJson(dao.getAllPharmacists()));
                case "medicines"     -> out.print(gson.toJson(dao.getAllMedicines()));
                default -> {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Unknown or missing action\"}");
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Server error: " + e.getMessage() + "\"}");
        }
    }
}
