package controller;

import com.google.gson.Gson;
import dao.PrescriptionDetailViewDAO;
import model.PrescriptionDetailView;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "HistoryPharmacistServlet", urlPatterns = {"/api/pharmacist/history"})
public class HistoryPharmacistServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8"); // Thiết lập kiểu phản hồi JSON

        // Lấy tham số từ request
        String patientName = request.getParameter("patientName");
        String prescriptionIdStr = request.getParameter("prescriptionId");

        Integer prescriptionId = null;

        // Kiểm tra và chuyển đổi prescriptionId sang kiểu số nguyên (nếu có)
        if (prescriptionIdStr != null && !prescriptionIdStr.isEmpty()) {
            try {
                prescriptionId = Integer.parseInt(prescriptionIdStr);
            } catch (NumberFormatException ignored) {
                // Bỏ qua nếu định dạng không hợp lệ
            }
        }

        // Gọi DAO để truy vấn dữ liệu chi tiết toa thuốc theo ID hoặc tên bệnh nhân
        PrescriptionDetailViewDAO dao = new PrescriptionDetailViewDAO();
        List<PrescriptionDetailView> list = dao.searchPrescriptionDetails(prescriptionId, patientName, "desc");

        // Chuyển danh sách kết quả sang JSON
        Gson gson = new Gson();
        String json = gson.toJson(list);

        // Gửi phản hồi về client
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}