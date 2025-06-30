package controller.payos;

import dao.CounterDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/api/update-payment")
public class UpdatePaymentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        try {
            JSONObject json = new JSONObject(req.getReader().lines().collect(Collectors.joining()));
            Long orderCode = json.getLong("orderCode");

            CounterDAO dao = new CounterDAO();
            int invoiceId = dao.findInvoiceIdByPid(orderCode);

            if (invoiceId == -1) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"status\":\"error\",\"error\":\"Không tìm thấy đơn hàng.\"}");
                return;
            }

            dao.updateInvoiceStatus(invoiceId, "paid");
            int prescriptionIdByInvoiceId = dao.findPrescriptionIdByInvoiceId(invoiceId);

            if(prescriptionIdByInvoiceId != -1){
                dao.updatePrescriptionStatus(prescriptionIdByInvoiceId, "Dispensed");
            }


            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"Cập nhật trạng thái đơn hàng thành công.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"status\":\"error\",\"error\":\"Lỗi xử lý dữ liệu.\"}");
        }
    }
}
