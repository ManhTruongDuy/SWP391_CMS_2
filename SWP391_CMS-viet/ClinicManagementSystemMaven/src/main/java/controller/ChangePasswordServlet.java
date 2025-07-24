package controller;

import dao.AccountStaffDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.AccountStaff;

import java.io.IOException;

@WebServlet("/changepassword")
public class ChangePasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        AccountStaff staff = (AccountStaff) session.getAttribute("account");

        if (staff == null) {
            resp.sendRedirect(req.getContextPath() + "/view/accountpharmacist/Login.jsp");
            return;
        }

        String email = staff.getEmail(); // dùng email làm khóa chính
        String oldPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        AccountStaffDAO dao = new AccountStaffDAO();

        if (!dao.checkCurrentPassword(email, oldPassword)) {
            req.setAttribute("message", "❌ Mật khẩu hiện tại không đúng!");
            req.getRequestDispatcher("/view/accountpharmacist/ChangePassword.jsp").forward(req, resp);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("message", "❌ Mật khẩu xác nhận không khớp!");
            req.getRequestDispatcher("/view/accountpharmacist/ChangePassword.jsp").forward(req, resp);
            return;
        }

        boolean changed = dao.changePassword(email, newPassword);
        if (changed) {
            session.invalidate(); // yêu cầu đăng nhập lại sau khi đổi mật khẩu
            req.setAttribute("message", "✅ Đổi mật khẩu thành công. Vui lòng đăng nhập lại.");
            req.getRequestDispatcher("/view/accountpharmacist/Login.jsp").forward(req, resp);
        } else {
            req.setAttribute("message", "❌ Đổi mật khẩu thất bại. Vui lòng thử lại.");
            req.getRequestDispatcher("/view/accountpharmacist/ChangePassword.jsp").forward(req, resp);
        }
    }
}
