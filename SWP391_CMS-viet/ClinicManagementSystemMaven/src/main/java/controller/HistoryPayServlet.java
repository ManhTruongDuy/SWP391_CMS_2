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
            // Khởi tạo DAO để thao tác dữ liệu từ database
            CounterDAO dao = new CounterDAO();

            // Lấy danh sách các hóa đơn có liên kết đến mã bệnh nhân (pid)
            List<Invoice> invoices = dao.getAllWithPid();

            // Dùng Gson để chuyển đổi danh sách Invoice thành JSON
            // LocalDateTime cần adapter vì không được Gson hỗ trợ mặc định
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();

            // Chuyển danh sách hóa đơn sang JSON
            String json = gson.toJson(invoices);

            // Thiết lập kiểu dữ liệu trả về là JSON, UTF-8
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            // Gửi JSON kết quả về cho client
            resp.getWriter().write(json);

        } catch (Exception e) {
            // Ghi log lỗi và trả về mã lỗi 500 (Internal Server Error)
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Lỗi khi lấy lịch sử thanh toán\"}");
        }
    }
}
