package controller;

// Thư viện Gson dùng để chuyển đổi Java object sang JSON và ngược lại
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// DAO xử lý truy vấn cơ sở dữ liệu liên quan đến thuốc và đơn thuốc
import dao.CounterDAO;

// Các thư viện Servlet của Jakarta EE
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Model đại diện cho thông tin thuốc trả về
import model.MedicineRes;

import java.io.IOException;
import java.util.List;

/**
 * Servlet xử lý API GET tại endpoint /api/historyPayDetail
 * Dùng để lấy danh sách thuốc tương ứng với mã đơn thuốc (invoiceId)
 */
@WebServlet("/api/historyPayDetail")
public class HistoryPayDetailServlet extends HttpServlet {

    /**
     * Xử lý HTTP GET request
     * Nhận vào tham số "id" (mã đơn thuốc), truy vấn dữ liệu thuốc từ DB,
     * và trả về kết quả dưới dạng JSON
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        // Kiểm tra nếu thiếu tham số "id" thì trả về lỗi 400
        if (idParam == null || idParam.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Thiếu mã đơn (invoiceId)\"}");
            return;
        }

        try {
            // Parse id từ String sang int
            int invoiceId = Integer.parseInt(idParam);

            // Gọi DAO để lấy danh sách thuốc theo mã đơn
            CounterDAO dao = new CounterDAO();
            List<MedicineRes> details = dao.getMedicineByInvoiceId(invoiceId);

            // Chuyển danh sách thuốc sang JSON, định dạng ngày là yyyy-MM-dd
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(details);

            // Thiết lập kiểu nội dung trả về là JSON và UTF-8
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            // Gửi kết quả JSON về client
            resp.getWriter().write(json);
        } catch (NumberFormatException e) {
            // Trường hợp mã đơn không hợp lệ (không phải số)
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Mã đơn không hợp lệ\"}");
        } catch (Exception e) {
            // Lỗi hệ thống hoặc lỗi truy vấn DB
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Lỗi xử lý dữ liệu\"}");
        }
    }
}
