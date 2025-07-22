/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
@WebServlet("/enterotp")
public class EnterOtpServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String generatedOtp = (String) session.getAttribute("otp");
        String email = (String) session.getAttribute("email");
        Long createdTime = (Long) session.getAttribute("otp_created_time");

        String userOtp = request.getParameter("otp");

        if (generatedOtp == null || email == null) {
            request.setAttribute("error", "Mã OTP đã hết hạn. Vui lòng gửi lại OTP.");
            request.getRequestDispatcher("/view/accountpharmacist/EnterOtp.jsp").forward(request, response);
            return;
        }

        //kiem tra thoi gian OTP
        long currentTime = System.currentTimeMillis();
        if (currentTime - createdTime > 2 * 60 * 1000) {

            request.setAttribute("error", "Mã OTP đã hết hạn, Vui lòng yêu cầu mã mới");
            session.removeAttribute("otp");
            session.removeAttribute("otp_created_time");
            request.getRequestDispatcher("/view/accountpharmacist/EnterOtp.jsp").forward(request, response);
            return;
        }

        if (generatedOtp.equals(userOtp)) {
            session.removeAttribute("otp");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/view/accountpharmacist/ResetPassword.jsp").forward(request, response);
        } else {

            String maskedEmail = email.replaceAll("(^[^@]{2})[^@]*(@.*$)", "$1****$2");
            request.setAttribute("error", "Mã OTP không chính xác. Vui lòng thử lại.");
            request.setAttribute("email", email);
            request.setAttribute("maskedEmail", maskedEmail);
            request.getRequestDispatcher("/view/accountpharmacist/EnterOtp.jsp").forward(request, response);
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
