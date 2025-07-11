package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.WarehouseDAO;
import model.MedicineWarehouse;
import model.Warehouse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/api/warehouse/*")
public class WarehouseServlet extends HttpServlet {
    private final WarehouseDAO warehouseDAO = new WarehouseDAO();
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            String[] pathParts = pathInfo.substring(1).split("/");
            if (pathParts.length == 1) {
                try {
                    int id = Integer.parseInt(pathParts[0]);
                    Warehouse warehouse = warehouseDAO.getWarehouseById(id);
                    if (warehouse != null) {
                        resp.getWriter().write(gson.toJson(warehouse));
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().write("{\"error\":\"Warehouse not found\"}");
                    }
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\":\"Invalid ID\"}");
                }
            } else if (pathParts.length == 2 && pathParts[1].equals("medicines")) {
                try {
                    int id = Integer.parseInt(pathParts[0]);
                    List<MedicineWarehouse> medicines = warehouseDAO.getMedicinesByWarehouseId(id);
                    System.out.println("Medicines fetched for warehouse " + id + ": " + medicines);
                    resp.getWriter().write(gson.toJson(medicines));
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\":\"Invalid ID\"}");
                } catch (Exception e) {
                    e.printStackTrace();
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("{\"error\":\"Failed to fetch medicines: " + e.getMessage() + "\"}");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Invalid endpoint\"}");
            }
        } else {
            try {
                resp.getWriter().write(gson.toJson(warehouseDAO.getAllWarehouses()));
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\":\"Failed to fetch warehouses\"}");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        Warehouse warehouse = gson.fromJson(req.getReader(), Warehouse.class);
        Warehouse addedWarehouse = warehouseDAO.addWarehouse(warehouse);
        if (addedWarehouse != null) {
            resp.getWriter().write(gson.toJson(addedWarehouse));
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Failed to add warehouse\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                Warehouse warehouse = gson.fromJson(req.getReader(), Warehouse.class);
                warehouse.setId(id);
                if (warehouseDAO.updateWarehouse(warehouse)) {
                    resp.getWriter().write(gson.toJson(warehouse));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\":\"Warehouse not found\"}");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Invalid ID\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID required for update\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                if (warehouseDAO.deleteWarehouse(id)) {
                    resp.getWriter().write("{\"message\":\"Warehouse deleted\"}");
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\":\"Warehouse not found\"}");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Invalid ID\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID required for deletion\"}");
        }
    }

    @Override
    public void init() throws ServletException {
        System.out.println("WarehouseServlet initialized successfully");
    }


}