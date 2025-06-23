package controller;

import com.google.gson.Gson;
import dao.WarehouseDAO;
import model.Warehouse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/warehouse/*")
public class WarehouseServlet extends HttpServlet {
    private final WarehouseDAO warehouseDAO = new WarehouseDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.split("/").length == 2) {
            try {
                int id = Integer.parseInt(pathInfo.split("/")[1]);
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
        } else {
            // Lấy tất cả kho hàng (tùy chọn)
            resp.getWriter().write(gson.toJson(warehouseDAO.getAllWarehouses()));
        }
    }
    @Override
    public void init() throws ServletException {
        System.out.println("WarehouseServlet initialized successfully");
    }
}