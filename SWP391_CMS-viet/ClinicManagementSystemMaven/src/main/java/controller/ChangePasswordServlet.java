package controller;

import dao.AccountPharmacistDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/changepassword")
public class ChangePasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        AccountPharmacistDAO dao = new AccountPharmacistDAO();

        if(!dao.checkCurrentPassword(username,oldPassword)){

            req.setAttribute("message","Mật khẩu cũ không chính xác!");
    req.getRequestDispatcher("/view/accountpharmacist/ChangePassword.jsp").forward(req,resp);
    return;
        }
        if(!newPassword.equals(confirmPassword)){

            req.setAttribute("message","Mật khẩu xác nhận không khớp");
            req.getRequestDispatcher("/view/accountpharmacist/ChangePassword.jsp").forward(req,resp);
            return;
        }
        boolean change = dao.changePassword(username,newPassword);
        if (change){
            req.getRequestDispatcher("/view/accountpharmacist/Login.jsp").forward(req,resp) ;



        }
        else{
            req.setAttribute("message","Đổi mật khẩu thất bại, vui lòng thử lại");


        }


    }
}
