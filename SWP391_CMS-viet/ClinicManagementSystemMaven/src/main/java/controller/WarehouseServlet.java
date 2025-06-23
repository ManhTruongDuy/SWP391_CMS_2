package controller;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Medicine;
import dao.WarehouseDAO;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/medicine")
public class WarehouseServlet extends HttpServlet {
    private WarehouseDAO dao = new WarehouseDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String action = req.getParameter("action");
        String keyword = req.getParameter("q");
        int page = parseInt(req.getParameter("page"), 1);
        int limit = parseInt(req.getParameter("limit"), 10);
        int offset = (page - 1) * limit;

        List<Medicine> data;
        if ("search".equalsIgnoreCase(action) && keyword != null) {
            data = dao.searchMedicines(keyword, limit, offset);
        } else if ("expired".equalsIgnoreCase(action)) {
            data = dao.getExpiredMedicines();
        } else {
            data = dao.listMedicines(limit, offset);
        }
        String json = gson.toJson(data);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        try {
            Medicine med = gson.fromJson(sb.toString(), Medicine.class);
            boolean success = dao.addMedicine(med);
            resp.getWriter().write("{\"success\": " + success + "}");
        } catch (Exception e) {
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private int parseInt(String val, int defaultVal) {
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
