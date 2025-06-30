package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.CounterDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MedicineRes;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/historyPayDetail")
public class HistoryPayDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Thiếu mã đơn (invoiceId)\"}");
            return;
        }

        try {
            int invoiceId = Integer.parseInt(idParam);

            CounterDAO dao = new CounterDAO();
            List<MedicineRes> details = dao.getMedicineByInvoiceId(invoiceId);

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(details);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(json);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Mã đơn không hợp lệ\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Lỗi xử lý dữ liệu\"}");
        }
    }
}
