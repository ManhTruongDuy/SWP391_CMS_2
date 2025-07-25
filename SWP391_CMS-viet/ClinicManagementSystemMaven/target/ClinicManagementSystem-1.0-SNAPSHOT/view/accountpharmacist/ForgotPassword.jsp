<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Forgot Password</title>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
  <style>
    :root {
      --primary: #4361ee;
      --primary-light: #4895ef;
      --primary-dark: #3a0ca3;
      --error: #f72585;
      --success: #4cc9f0;
      --gray: #f8f9fa;
      --dark-gray: #adb5bd;
    }

    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: 'Poppins', sans-serif;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      padding: 20px;
    }

    .form-container {
      background-color: white;
      padding: 40px;
      border-radius: 16px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
      width: 100%;
      max-width: 450px;
      text-align: center;
      transition: all 0.3s ease;
    }

    .form-container:hover {
      box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
    }

    h2 {
      color: #2b2d42;
      margin-bottom: 10px;
      font-weight: 600;
    }

    .subtitle {
      color: var(--dark-gray);
      margin-bottom: 30px;
      font-size: 14px;
      line-height: 1.5;
    }

    .input-group {
      margin-bottom: 25px;
      text-align: left;
    }

    .input-group label {
      display: block;
      margin-bottom: 8px;
      color: #495057;
      font-size: 14px;
      font-weight: 500;
    }

    input[type="text"] {
      width: 100%;
      padding: 12px 16px;
      border: 2px solid #e9ecef;
      border-radius: 8px;
      font-size: 14px;
      transition: all 0.3s;
      box-sizing: border-box;
    }

    input[type="text"]:focus {
      outline: none;
      border-color: var(--primary-light);
      box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.1);
    }

    input[type="text"]::placeholder {
      color: var(--dark-gray);
    }

    button {
      background-color: var(--primary);
      color: white;
      padding: 12px 24px;
      border: none;
      border-radius: 8px;
      cursor: pointer;
      font-size: 15px;
      font-weight: 500;
      width: 100%;
      transition: all 0.3s;
      box-shadow: 0 4px 6px rgba(67, 97, 238, 0.1);
    }

    button:hover {
      background-color: var(--primary-dark);
      transform: translateY(-2px);
      box-shadow: 0 6px 12px rgba(67, 97, 238, 0.15);
    }

    button:active {
      transform: translateY(0);
    }

    .error {
      color: var(--error);
      margin: 15px 0;
      font-size: 14px;
      padding: 10px;

      border-radius: 6px;
    }

    .message {
      color: var(--success);
      margin: 15px 0;
      font-size: 14px;
      padding: 10px;

      border-radius: 6px;
    }

    .footer-links {
      margin-top: 25px;
      display: flex;
      justify-content: center;
      gap: 15px;
    }

    .link {
      color: var(--primary);
      text-decoration: none;
      font-size: 13px;
      font-weight: 500;
      transition: all 0.2s;
    }

    .link:hover {
      color: var(--primary-dark);
      text-decoration: underline;
    }

    .link i {
      margin-right: 5px;
    }

    @media (max-width: 480px) {
      .form-container {
        padding: 30px 20px;
      }
    }
  </style>
</head>
<body>
<div class="form-container">

  <h2>Bạn quên mật khẩu?</h2>
  <p class="subtitle">Nhập địa chỉ email của bạn và chúng tôi sẽ gửi cho bạn mã xác minh để đặt lại mật khẩu.</p>

  <form action="<%=request.getContextPath()%>/forgotpassword" method="POST">
    <div class="input-group">
      <label for="email">Địa chỉ email</label>
      <input type="text" id="email" name="email" placeholder="Địa chỉ email của bạn?"
             value="${email != null ? email : ''}">
      <p id="emailError" class="error" style="display: none;">Vui lòng nhập địa chỉ email hợp lệ.</p>
    </div>

    <button type="submit">Gửi mã xác minh</button>

    <c:if test="${not empty error}">
      <p class="error">${error}</p>
    </c:if>
    <c:if test="${not empty message}">
      <p class="message">${message}</p>
    </c:if>
  </form>

  <div class="footer-links">
    <a href="${pageContext.request.contextPath}/view/accountpharmacist/Login.jsp"
       class="link"
       onclick="return confirm('Bạn có chắc chắn muốn quay lại trang đăng nhập không?');">
      <i>←</i> Quay lại trang đăng nhập
    </a>  </div>
</div>

<script>
  document.querySelector("form").addEventListener("submit", function (e) {
    const emailInput = document.getElementById("email");
    const emailError = document.getElementById("emailError");
    const emailValue = emailInput.value.trim();
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;


    if (!emailPattern.test(emailValue)) {
      e.preventDefault();
      emailError.style.display = "block";
      emailInput.focus();
    } else {
      emailError.style.display = "none";
    }
  });
</script>
</body>
</html>
