package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.CounterDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Invoice;
import util.LocalDateTimeAdapter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/api/historyPay")
public class HistoryPayServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            CounterDAO dao = new CounterDAO();
            List<Invoice> invoices = dao.getAllWithPid();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();

            String json = gson.toJson(invoices);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(json);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Lỗi khi lấy lịch sử thanh toán\"}");
        }
    }
}
