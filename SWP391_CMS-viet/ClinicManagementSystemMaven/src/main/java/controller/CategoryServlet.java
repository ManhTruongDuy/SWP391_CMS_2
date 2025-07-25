package controller;

import com.google.gson.Gson;
import dao.CategoryDAO;
import model.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/categories/*")
public class CategoryServlet extends HttpServlet {
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final Gson gson = new Gson();

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
        try (PrintWriter out = resp.getWriter()) {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Category> categories = categoryDAO.getAllCategories();
                out.write(gson.toJson(categories));
            } else {
                try {
                    int id = Integer.parseInt(pathInfo.substring(1));
                    Category category = categoryDAO.getCategoryById(id);
                    if (category != null) {
                        out.write(gson.toJson(category));
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.write("{\"error\":\"Category not found\"}");
                    }
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("{\"error\":\"Invalid ID\"}");
                }
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Failed to fetch categories: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
            }
            Category category = gson.fromJson(sb.toString(), Category.class);
            if (category != null && category.getCategoryName() != null && !category.getCategoryName().trim().isEmpty()) {
                if (categoryDAO.addCategory(category)) {
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    out.print(gson.toJson(category));
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Insert failed\"}");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Category name is required\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid body or data: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        try (PrintWriter out = resp.getWriter()) {
            if (pathInfo == null || !pathInfo.matches("/\\d+")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"error\":\"Invalid or missing ID\"}");
                return;
            }
            int id = Integer.parseInt(pathInfo.substring(1));
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
            }
            Category category = gson.fromJson(sb.toString(), Category.class);
            if (category != null && category.getCategoryName() != null && !category.getCategoryName().trim().isEmpty()) {
                category.setCategory_id(id); // Gán ID từ path
                if (categoryDAO.updateCategory(category)) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    out.write(gson.toJson(category));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write("{\"error\":\"Update failed\"}");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"error\":\"Category name is required\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid body or data: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.length() <= 1) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                new Gson().toJson(new ErrorResponse("ID danh mục không hợp lệ"), resp.getWriter());
                return;
            }
            int id = Integer.parseInt(pathInfo.substring(1));
            boolean success = categoryDAO.deleteCategory(id);
            if (success) {
                resp.setStatus(HttpServletResponse.SC_OK);
                new Gson().toJson(new SuccessResponse("Xóa danh mục thành công"), resp.getWriter());
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                new Gson().toJson(new ErrorResponse("Danh mục không tồn tại"), resp.getWriter());
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            new Gson().toJson(new ErrorResponse(e.getMessage()), resp.getWriter());
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            new Gson().toJson(new ErrorResponse("ID danh mục không hợp lệ"), resp.getWriter());
        }
    }

    // Inner class for error response
    private static class ErrorResponse {
        String error;
        ErrorResponse(String error) {
            this.error = error;
        }
    }

    // Inner class for success response
    private static class SuccessResponse {
        String message;
        SuccessResponse(String message) {
            this.message = message;
        }
    }
}