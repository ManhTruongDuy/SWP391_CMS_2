package controller;

import com.google.gson.GsonBuilder;
import dao.HistoryDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import model.History;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/api/history")
public class HistoryServlet extends HttpServlet {
    private final HistoryDAO historyDAO = new HistoryDAO(); // Khởi tạo trực tiếp
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            String actionType = req.getParameter("actionType");
            String entityType = req.getParameter("entityType");
            String date = req.getParameter("date");
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
            int pageSize = req.getParameter("pageSize") != null ? Integer.parseInt(req.getParameter("pageSize")) : 10;

            if (page < 1 || pageSize < 1) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Page hoặc pageSize không hợp lệ\"}");
                return;
            }

            List<History> histories = historyDAO.searchHistories(actionType, entityType, date, page, pageSize);
            int totalItems = historyDAO.countHistories(actionType, entityType, date);

            HistoryResponse response = new HistoryResponse(histories, totalItems);
            out.print(gson.toJson(response));
        } catch (NumberFormatException e) {
            log("Lỗi định dạng page hoặc pageSize: " + e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Page hoặc pageSize không hợp lệ\"}");
        } catch (Exception e) {
            log("Lỗi không xác định: " + e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Lỗi không xác định: " + e.getMessage() + "\"}");
        }
    }

    private static class HistoryResponse {
        List<History> histories;
        int totalItems;

        HistoryResponse(List<History> histories, int totalItems) {
            this.histories = histories;
            this.totalItems = totalItems;
        }
    }
}