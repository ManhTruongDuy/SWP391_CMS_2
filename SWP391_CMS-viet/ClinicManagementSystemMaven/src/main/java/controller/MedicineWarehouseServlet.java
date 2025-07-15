package controller;

import dao.MedicineDAO;
import dao.MedicineWarehouseDAO;

import model.Medicine;
import com.google.gson.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

@WebServlet("/api/MedicineWarehouse/*")
public class MedicineWarehouseServlet extends HttpServlet {
    private final MedicineWarehouseDAO dao = new MedicineWarehouseDAO();
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        String name = req.getParameter("name");

        try {
            // Handle search by name
            if (name != null && !name.trim().isEmpty()) {
                try {
                    List<Medicine> medicines = dao.getMedicineByName(name);
                    if (medicines != null && !medicines.isEmpty()) {
                        resp.getWriter().write(gson.toJson(medicines));
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().write("{\"error\":\"No warehouses found with name: " + name + "\"}");
                    }
                } catch (Exception e) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("{\"error\":\"Failed to fetch warehouses by name: " + e.getMessage() + "\"}");
                }
                return;
            }
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.isEmpty()) {

                List<Medicine> medicines = dao.getAllMedicines();
                String json = gson.toJson(medicines);
                resp.getWriter().write(json);
            } else {

                String[] parts = pathInfo.split("/");
                if (parts.length == 2) {
                    int id = Integer.parseInt(parts[1]);
                    Medicine medicine = dao.getMedicineById(id);
                    if (medicine != null) {
                        String json = gson.toJson(medicine);
                        resp.getWriter().write(json);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().write("{\"error\":\"Medicine not found\"}");
                    }
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\":\"Invalid URL format\"}");
                }
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid ID\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Server error: " + e.getMessage() + "\"}");
        }
    }

    // POST: thêm thuốc mới
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }
        try {
            Medicine med = gson.fromJson(sb.toString(), Medicine.class);
            boolean ok = dao.addMedicine(med);
            if (ok) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                out.print(gson.toJson(med));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Insert failed\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid body or data: " + e.getMessage() + "\"}");
        }
        out.flush();
    }

    // PUT: cập nhật thuốc (theo ID trên url, truyền JSON body)
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.split("/").length == 2) {
            try {
                int id = Integer.parseInt(pathInfo.split("/")[1]);
                StringBuilder sb = new StringBuilder();
                try (BufferedReader reader = req.getReader()) {
                    String line;
                    while ((line = reader.readLine()) != null) sb.append(line);
                }
                Medicine med = gson.fromJson(sb.toString(), Medicine.class);
                med.setMedicine_id(id);
                boolean ok = dao.updateMedicine(med);
                if (ok) {
                    out.print(gson.toJson(med));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Medicine not found or update failed\"}");
                }
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Invalid ID or update failed\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid request\"}");
        }
        out.flush();
    }
    // DELETE: xóa thuốc
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        PrintWriter out = resp.getWriter();
        if (pathInfo != null && pathInfo.split("/").length == 2) {
            try {
                int id = Integer.parseInt(pathInfo.split("/")[1]);
                boolean ok = dao.deleteMedicine(id);
                if (ok) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Medicine not found\"}");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Invalid ID\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid request\"}");
        }
        out.flush();
    }

    @Override
    public void init() throws ServletException {
        System.out.println("MedicineWarehouse initialized successfully");
    }

}