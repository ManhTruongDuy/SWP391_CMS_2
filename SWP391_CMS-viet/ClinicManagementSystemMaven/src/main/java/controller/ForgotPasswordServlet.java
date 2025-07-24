package controller;

import dao.AccountPharmacistDAO;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

@WebServlet("/forgotpassword")
public class ForgotPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String resend = request.getParameter("resend");
        String email = request.getParameter("email");

        HttpSession session = request.getSession();

        if ("true".equals(resend) && email != null) {
            Long previousTime = (Long) session.getAttribute("otp_created_time");

            // Kiểm tra thời gian tồn tại của OTP
            if (previousTime != null && System.currentTimeMillis() - previousTime < 2 * 60 * 1000) {
                request.setAttribute("error", "Bạn chỉ có thể yêu cầu lại OTP sau 2 phút.");
            } else {
                // Tạo và gửi OTP mới
                session.removeAttribute("otp_created_time"); // Reset thời gian cũ trước khi tạo mới
                generateAndSendOtp(session, email, request);
            }

            String maskedEmail = email.replaceAll("(^[^@]{2})[^@]*(@.*$)", "$1****$2");
            request.setAttribute("email", email);
            request.setAttribute("maskedEmail", maskedEmail);
            request.getRequestDispatcher("/view/accountpharmacist/EnterOtp.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/view/accountpharmacist/ForgotPassword.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        request.setAttribute("email", email);
        HttpSession session = request.getSession();
        AccountPharmacistDAO dao = new AccountPharmacistDAO();

        // Kiểm tra email có tồn tại trong DB không
        if (!dao.isEmailExist(email)) {
            request.setAttribute("error", "Không tìm thấy tài khoản.");
            request.getRequestDispatcher("/view/accountpharmacist/ForgotPassword.jsp").forward(request, response);
            return;
        }

        // **Reset dữ liệu cũ khi nhập email mới**
        session.removeAttribute("otp_created_time"); // Xóa thời gian OTP cũ nếu có
        session.removeAttribute("otp"); // Xóa mã OTP cũ
        session.removeAttribute("email"); // Xóa email cũ để đảm bảo đồng bộ

        // **Sinh OTP mới và gắn lại vào session**
        generateAndSendOtp(session, email, request);

        // Gửi lại thông tin email cho trang EnterOtp.jsp
        request.setAttribute("email", email);
        request.getRequestDispatcher("/view/accountpharmacist/EnterOtp.jsp").forward(request, response);
    }

    private void generateAndSendOtp(HttpSession session, String email, HttpServletRequest request) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000)); // Tạo mã OTP
        session.setAttribute("otp", otp);
        session.setAttribute("email", email);
        session.setAttribute("otp_created_time", System.currentTimeMillis()); // Lưu thời gian tạo OTP

        // Gửi OTP qua email
        boolean sent = sendOtpEmail(email, otp);
        if (sent) {
            request.setAttribute("message", "Mã OTP đã được gửi.");
        } else {
            request.setAttribute("error", "Không thể gửi OTP. Vui lòng thử lại sau.");
        }
    }

    private boolean sendOtpEmail(String toEmail, String otp) {
        final String fromEmail = "doduong05042004@gmail.com";
        final String password = "hswc afzi rcar yjsc"; // App password Gmail

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "OTP Service", "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(MimeUtility.encodeText("Xác nhận OTP", "UTF-8", "B"));
            message.setContent("Mã OTP của bạn là: " + otp + ". Vui lòng không chia sẻ với bất kỳ ai.", "text/plain; charset=UTF-8");

            Transport.send(message);
            return true;
        } catch (Exception e) {
            System.out.println("Gửi email thất bại: " + e.getMessage());
            return false;
        }
    }
}