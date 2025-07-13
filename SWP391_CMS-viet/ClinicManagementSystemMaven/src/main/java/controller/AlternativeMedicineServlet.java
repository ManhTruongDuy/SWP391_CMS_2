package controller;

import com.google.gson.Gson;
import dao.MedicineDAO;
import model.Medicine;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AlternativeMedicineServlet", urlPatterns = {"/api/pharmacist/alternatives", "/api/pharmacist/medicine-by-name"})
public class AlternativeMedicineServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8"); // Thiết lập kiểu phản hồi là JSON

        String path = request.getServletPath(); // Lấy đường dẫn để xác định API nào được gọi
        MedicineDAO dao = new MedicineDAO(); // Khởi tạo DAO để truy vấn dữ liệu thuốc
        Gson gson = new Gson(); // Dùng để chuyển đổi danh sách thuốc sang JSON
        PrintWriter out = response.getWriter(); // Đối tượng để ghi dữ liệu phản hồi

        // Xử lý khi gọi API tìm thuốc theo thành phần hoạt chất
        if ("/api/pharmacist/alternatives".equals(path)) {
            String ingredient = request.getParameter("ingredient"); // Lấy tham số "ingredient" từ URL
            List<Medicine> list = dao.findMedicinesByIngredient(ingredient); // Truy vấn thuốc theo hoạt chất
            out.print(gson.toJson(list)); // Gửi danh sách thuốc dưới dạng JSON về client
        }
        // Xử lý khi gọi API tìm thuốc theo tên
        else if ("/api/pharmacist/medicine-by-name".equals(path)) {
            String name = request.getParameter("name"); // Lấy tham số "name" từ URL
            List<Medicine> list = dao.findMedicinesByName(name); // Truy vấn thuốc theo tên
            out.print(gson.toJson(list)); // Gửi danh sách thuốc dưới dạng JSON về client
        }

        out.flush(); // Đảm bảo dữ liệu được ghi đầy đủ
    }
}