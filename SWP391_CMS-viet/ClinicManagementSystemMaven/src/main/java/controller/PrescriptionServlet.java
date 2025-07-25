package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.PrescriptionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Prescription;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/prescriptions")
public class PrescriptionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        PrescriptionDAO dao = new PrescriptionDAO();

        String idParam = req.getParameter("id");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Prescription pre = dao.getPrescriptionById(id);
                if (pre != null) {
                    resp.getWriter().write(gson.toJson(pre));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\":\"Prescription not found\"}");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Invalid prescription ID\"}");
            }
            return;
        }

        int page = 1;
        int pageSize = 10;

        try {
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
            }
            if (req.getParameter("pageSize") != null) {
                pageSize = Integer.parseInt(req.getParameter("pageSize"));
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid page or pageSize\"}");
            return;
        }

        String status = req.getParameter("status");
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");

        java.sql.Date startDate = null;
        java.sql.Date endDate = null;
        try {
            if (startDateStr != null) startDate = java.sql.Date.valueOf(startDateStr);
            if (endDateStr != null) endDate = java.sql.Date.valueOf(endDateStr);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid date format. Use yyyy-MM-dd\"}");
            return;
        }

        List<Prescription> prescriptions = dao.getAllPrescriptions(page, pageSize, status, startDate, endDate);
        int totalItems = dao.getTotalPrescriptions();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("data", prescriptions);
        result.put("totalItems", totalItems);
        result.put("totalPages", totalPages);
        result.put("currentPage", page);

        resp.getWriter().write(gson.toJson(result));
    }

}
