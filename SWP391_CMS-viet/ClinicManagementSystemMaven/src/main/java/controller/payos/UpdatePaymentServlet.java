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
            JSONObject json = new JSONObject(req.getReader().lines().collect(Collectors.joining())); // Đọc toàn bộ nội dung JSON từ body của request và chuyển thành một đối tượng JSONObject
            Long orderCode = json.getLong("orderCode"); // Trích xuất trường "orderCode" từ JSON, kiểu Long (mã đơn hàng)

            CounterDAO dao = new CounterDAO(); // Tạo instance của DAO để làm việc với dữ liệu (truy vấn database)
            int invoiceId = dao.findInvoiceIdByPid(orderCode); // Tìm mã hóa đơn (invoiceId) tương ứng với mã đơn hàng (orderCode)

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

// --- TỰ ĐỘNG TRỪ SỐ LƯỢNG THUỐC ---
            // Lấy danh sách thuốc đã bán trong hóa đơn
            java.util.List<model.MedicineRes> soldMedicines = dao.getMedicineByInvoiceId(invoiceId);
            dao.MedicineDAO medicineDAO = new dao.MedicineDAO();
            for (model.MedicineRes sold : soldMedicines) {
                model.Medicine med = medicineDAO.getMedicineById(sold.getId());
                if (med != null) {
                    int newQuantity = med.getQuantity() - sold.getQuantity();
                    if (newQuantity < 0) newQuantity = 0; // Không cho âm kho
                    med.setQuantity(newQuantity);
                    medicineDAO.updateMedicine(med);
                }
            }
            // --- END ---
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"Cập nhật trạng thái đơn hàng thành công.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"status\":\"error\",\"error\":\"Lỗi xử lý dữ liệu.\"}");
        }
    }
}
