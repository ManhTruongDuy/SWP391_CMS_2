<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.AccountStaff" %>

<%
    AccountStaff staff = (AccountStaff) session.getAttribute("account");
    if (staff == null) {
        response.sendRedirect(request.getContextPath() + "/view/accountpharmacist/Login.jsp");
        return;
    }
    int staffId = staff.getAccount_staff_id();

    // Lấy thông báo nếu có
    String message = (String) request.getAttribute("message");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đổi Mật Khẩu</title>
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@400;600&display=swap" rel="stylesheet">
    <style>
        * { box-sizing: border-box; }
        body {
            margin: 0;
            font-family: 'Quicksand', sans-serif;
            background: linear-gradient(to right, #d0eaff, #f0faff);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .form-container {
            background-color: white;
            padding: 40px 30px;
            border-radius: 20px;
            box-shadow: 0 10px 30px rgba(0, 128, 255, 0.15);
            width: 100%;
            max-width: 400px;
            text-align: center;
        }
        h2 { margin-bottom: 15px; color: #007acc; }
        .notice {
            font-size: 14px;
            margin-bottom: 20px;
            padding: 12px;
            border-radius: 8px;
            text-align: left;
        }
        .notice.success {
            color: #006600;
            background-color: #e6ffe6;
            border: 1px solid #99cc99;
        }
        .notice.error {
            color: #990000;
            background-color: #ffe6e6;
            border: 1px solid #ff9999;
        }
        .message {
            font-size: 14px;
            margin-bottom: 20px;
            color: #005c99;
            background-color: #e6f3ff;
            padding: 12px;
            border-radius: 8px;
            border: 1px solid #99ccff;
            text-align: left;
        }
        .input-wrapper { position: relative; margin-bottom: 20px; }
        input[type="password"] {
            width: 100%;
            padding: 12px;
            padding-right: 12px;
            border: 2px solid #007acc;
            border-radius: 10px;
            font-size: 15px;
            font-family: inherit;
        }
        input[type="password"]:focus {
            outline: none;
            border-color: #005fa3;
        }
        .checkmark {
            position: absolute;
            right: 12px;
            top: 50%;
            transform: translateY(-50%);
            font-size: 18px;
            display: none;
        }
        .checkmark.valid { display: inline; color: green; }
        .checkmark.invalid { display: inline; color: red; }
        .footer-links {
            margin-top: 25px;
            display: flex;
            justify-content: center;
            gap: 15px;
        }
        button[type="submit"] {
            background: linear-gradient(to right, #00b4db, #0083b0);
            color: white;
            padding: 12px 26px;
            border: none;
            border-radius: 10px;
            cursor: pointer;
            font-size: 16px;
            transition: 0.3s ease;
            margin-top: 10px;
            width: 100%;
        }
        button:hover {
            background: linear-gradient(to right, #0099cc, #006699);
        }
    </style>
</head>
<body>
<div class="form-container">
    <h2>Đổi Mật Khẩu</h2>

    <% if (message != null) { %>
    <div class="notice <%= message.startsWith("✅") ? "success" : "error" %>">
        <%= message %>
    </div>
    <% } %>

    <div class="message">
        🔒 Mật khẩu mới phải từ <strong>8–32 ký tự</strong>, có chữ hoa, chữ thường, số và ký tự đặc biệt.
    </div>

    <form id="changeForm" action="<%= request.getContextPath() %>/changepassword" method="post" onsubmit="return validateForm();">
        <input type="hidden" name="staffId" value="<%= staffId %>" />

        <div class="input-wrapper">
            <input type="password" id="currentPassword" name="currentPassword" placeholder="Mật khẩu hiện tại" required>
        </div>

        <div class="input-wrapper">
            <input type="password" id="newPassword" name="newPassword" placeholder="Mật khẩu mới" required>
            <span id="checkPass" class="checkmark">✔</span>
        </div>

        <div class="input-wrapper">
            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Xác nhận mật khẩu mới" required>
            <span id="checkConfirm" class="checkmark">✔</span>
        </div>

        <button type="submit">Đổi Mật Khẩu</button>

        <div class="footer-links">
            <a href="<%= request.getContextPath() %>/staff/profile.jsp"
               class="link"
               onclick="return confirm('Bạn có chắc chắn muốn quay lại trang hồ sơ không?');">
                ← Quay lại hồ sơ
            </a>
        </div>
    </form>
</div>

<script>
    const newPass = document.getElementById("newPassword");
    const confirmPass = document.getElementById("confirmPassword");
    const checkPass = document.getElementById("checkPass");
    const checkConfirm = document.getElementById("checkConfirm");

    function isStrongPassword(password) {
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,32}$/;
        return regex.test(password);
    }

    function validateForm() {
        const pass = newPass.value.trim();
        const confirm = confirmPass.value.trim();

        if (!isStrongPassword(pass)) {
            alert("Mật khẩu mới không đủ mạnh.");
            return false;
        }
        if (pass !== confirm) {
            alert("Mật khẩu xác nhận không khớp.");
            return false;
        }
        return true;
    }

    function updateCheckmarks() {
        const pass = newPass.value.trim();
        const confirm = confirmPass.value.trim();

        if (isStrongPassword(pass)) {
            checkPass.textContent = "✔";
            checkPass.className = "checkmark valid";
        } else {
            checkPass.textContent = "✖";
            checkPass.className = "checkmark invalid";
        }

        if (confirm && pass === confirm) {
            checkConfirm.textContent = "✔";
            checkConfirm.className = "checkmark valid";
        } else {
            checkConfirm.textContent = "✖";
            checkConfirm.className = "checkmark invalid";
        }
    }

    newPass.addEventListener("input", updateCheckmarks);
    confirmPass.addEventListener("input", updateCheckmarks);
</script>
</body>
</html>
