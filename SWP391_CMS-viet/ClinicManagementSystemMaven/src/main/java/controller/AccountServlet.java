package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.PharmacistDAO;
import model.Pharmacist;
import model.PharmacistDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/account")
public class AccountServlet extends HttpServlet {
    private PharmacistDAO pharmacistDAO;
    private final Gson gson = new GsonBuilder().create();

    @Override
    public void init() throws ServletException {
        try {
            pharmacistDAO = new PharmacistDAO(dao.DBContext.getInstance().getConnection());
            System.out.println("AccountServlet được khởi tạo thành công với kết nối từ DBContext");
        } catch (Exception e) {
            throw new ServletException("Không thể khởi tạo PharmacistDAO: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (pharmacistDAO == null) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"PharmacistDAO chưa được khởi tạo\"}");
            return;
        }

        try {
            PharmacistDTO accountData = gson.fromJson(req.getReader(), PharmacistDTO.class);
            String action = accountData.getAction(); // Sử dụng action để xác định hành động

            if ("create".equalsIgnoreCase(action)) {
                // Xử lý tạo tài khoản
                String name = accountData.getName();
                String mobile = accountData.getMobile();
                String email = accountData.getEmail();
                String password = accountData.getPassword();

                if (name == null || mobile == null || email == null || password == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\":\"Thiếu thông tin bắt buộc\"}");
                    return;
                }

                Pharmacist pharmacist = new Pharmacist();
                if (!pharmacist.emailChecker(email)) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\":\"Định dạng email không hợp lệ\"}");
                    return;
                }
                if (!pharmacist.mobileChecker(mobile)) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\":\"Số điện thoại không hợp lệ\"}");
                    return;
                }

                pharmacistDAO.create(name, mobile, email, password);
                resp.getWriter().write(gson.toJson("Tài khoản được tạo thành công"));
            } else if ("update".equalsIgnoreCase(action)) {
                // Xử lý cập nhật thông tin
                int pharmacistId = accountData.getPharmacistId();
                String fullName = accountData.getName();
                String phone = accountData.getMobile();
                int accountPharmacistId = accountData.getAccountPharmacistId();
                String username = accountData.getUsername();
                String email = accountData.getEmail();
                String password = accountData.getPassword();
                String status = accountData.getStatus() != null ? accountData.getStatus() : "Enable"; // Giá trị mặc định
                String img = accountData.getImg() != null ? accountData.getImg() : "https://randomuser.me/api/portraits/men/1.jpg"; // Giá trị mặc định

                if (pharmacistId <= 0 || accountPharmacistId <= 0) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\":\"pharmacistId hoặc accountPharmacistId không hợp lệ\"}");
                    return;
                }

                if (fullName == null || phone == null || username == null || email == null || password == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\":\"Thiếu thông tin bắt buộc\"}");
                    return;
                }

                pharmacistDAO.update(pharmacistId, fullName, phone, accountPharmacistId, username, email, password, status, img);
                resp.getWriter().write(gson.toJson("Thông tin được cập nhật thành công"));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Hành động không hợp lệ\"}");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Email đã tồn tại\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\":\"Không thể xử lý yêu cầu: " + e.getMessage() + "\"}");
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("AccountServlet destroyed");
    }
}