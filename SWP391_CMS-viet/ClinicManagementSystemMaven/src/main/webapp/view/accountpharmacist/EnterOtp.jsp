<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Xác Thực OTP</title>
  <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@400;600&display=swap" rel="stylesheet">
  <style>
    * { box-sizing: border-box; }

    body {
      margin: 0;
      font-family: 'Quicksand', sans-serif;
      background: linear-gradient(to right, #a1c4fd, #c2e9fb);
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }

    .container {
      background-color: #fff;
      padding: 40px 30px;
      border-radius: 20px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
      text-align: center;
      width: 100%;
      max-width: 420px;
      animation: fadeIn 0.5s ease;
    }

    @keyframes fadeIn {
      from { opacity: 0; transform: scale(0.95); }
      to { opacity: 1; transform: scale(1); }
    }

    h2 { color: #333; margin-bottom: 20px; }

    .info-email {
      font-size: 15px;
      color: #666;
      margin-bottom: 15px;
    }

    .countdown {
      font-size: 15px;
      color: #ff4d4f;
      margin-bottom: 20px;
    }

    .otp-input {
      display: flex;
      justify-content: center;
      gap: 12px;
      margin-bottom: 25px;
    }

    .otp-input input {
      width: 50px;
      height: 60px;
      font-size: 26px;
      text-align: center;
      border: 2px solid #86b7fe;
      border-radius: 12px;
      transition: all 0.2s ease-in-out;
      outline: none;
    }

    .otp-input input:focus {
      border-color: #007bff;
      box-shadow: 0 0 8px rgba(0, 123, 255, 0.3);
    }

    button {
      background: linear-gradient(135deg, #ff7e5f, #feb47b);
      color: white;
      padding: 14px 28px;
      border: none;
      border-radius: 12px;
      cursor: pointer;
      font-size: 16px;
      transition: background 0.3s ease;
    }

    button:disabled {
      background-color: #ccc;
      cursor: not-allowed;
    }

    button:hover:not(:disabled) {
      background: linear-gradient(135deg, #f86a3c, #fd9963);
    }

    .resend {
      margin-top: 18px;
      font-size: 14px;
      color: #444;
    }

    .resend a {
      color: #007bff;
      text-decoration: none;
      font-weight: 600;
      cursor: pointer;
    }

    .resend a:hover {
      text-decoration: underline;
    }

    .error {
      color: #dc3545;
      margin-top: 10px;
      font-size: 14px;
    }

    .success {
      color: #28a745;
      margin-top: 10px;
      font-size: 14px;
    }

    img {
      margin-top: 15px;
      width: 70px;
    }
  </style>
</head>
<body>
<div class="container">
  <h2>XÁC THỰC OTP</h2>

  <p class="info-email">
    Vui lòng nhập mã số chúng tôi đã gửi qua email<br>
    <strong>${maskedEmail}</strong>.
  </p>

  <p class="countdown">Mã OTP sẽ hết hạn sau <span id="timer">120</span> giây</p>

  <form action="<%=request.getContextPath()%>/enterotp" method="post" onsubmit="return submitOtp();">
    <div class="otp-input">
      <input type="text" maxlength="1" name="d1" oninput="move(this, 'd2')" required>
      <input type="text" maxlength="1" name="d2" oninput="move(this, 'd3')" required>
      <input type="text" maxlength="1" name="d3" oninput="move(this, 'd4')" required>
      <input type="text" maxlength="1" name="d4" oninput="move(this, 'd5')" required>
      <input type="text" maxlength="1" name="d5" oninput="move(this, 'd6')" required>
      <input type="text" maxlength="1" name="d6" required>
    </div>

    <input type="hidden" id="otp" name="otp">
    <button type="submit" id="submitBtn">Tiếp tục</button>

    <c:if test="${not empty error}">
      <p class="error">${error}</p>
    </c:if>
    <c:if test="${not empty message}">
      <p class="success">${message}</p>
    </c:if>
  </form>

  <div class="resend">
    Chưa nhận được mã? <a id="resendLink" href="<%=request.getContextPath()%>/forgotpassword?resend=true&email=${email}">Gửi lại</a>
  </div>

  <div class="resend" style="margin-top: 10px;">
    <a href="<%=request.getContextPath()%>/loginservlet" onclick="return confirmBack()">Quay lại trang đăng nhập</a>
  </div>
</div>

<script>
  const OTP_DURATION = 120; // 120 giây đếm ngược
  const timerDisplay = document.getElementById("timer"); // Thẻ hiển thị thời gian
  const submitBtn = document.getElementById("submitBtn"); // Nút gửi OTP
  const resendLink = document.getElementById("resendLink"); // Nút gửi lại OTP

  // Lấy email từ session, đây là email đang sử dụng tài khoản hiện tại
  const email = `<%= session.getAttribute("email") %>`;
  const STORAGE_KEY = `otp_expire_time_${email}`; // Tạo khóa duy nhất cho tài khoản hiện tại
  let expireTime;

  // Hàm xóa dữ liệu cũ khi đổi tài khoản
  function cleanExpiredTimer(prevEmail) {
    const oldKey = `otp_expire_time_${prevEmail}`;
    if (localStorage.getItem(oldKey)) {
      localStorage.removeItem(oldKey); // Xóa dữ liệu của tài khoản cũ
    }
  }

  // Lấy thông tin expireTime cho tài khoản hiện tại
  function getExpireTime() {
    const currentTime = new Date().getTime();
    let storedTime = parseInt(localStorage.getItem(STORAGE_KEY)); // Lấy expireTime từ localStorage

    // Nếu không tồn tại hoặc đã hết hạn
    if (!storedTime || currentTime >= storedTime) {
      storedTime = currentTime + OTP_DURATION * 1000; // Thiết lập 120s mới
      localStorage.setItem(STORAGE_KEY, storedTime); // Cập nhật localStorage
    }
    return storedTime;
  }

  // Khởi động bộ đếm ngược
  function startCountdown() {
    expireTime = getExpireTime(); // Lấy expiredTime hiện tại
    const countdown = setInterval(() => {
      const now = new Date().getTime();
      const timeLeft = Math.max(0, Math.ceil((expireTime - now) / 1000)); // Tính thời gian còn lại

      timerDisplay.textContent = timeLeft; // Cập nhật UI thời gian còn lại

      if (timeLeft <= 0) {
        // Khi hết thời gian
        clearInterval(countdown); // Dừng bộ đếm
        timerDisplay.textContent = "0"; // Hiển thị "0"
        submitBtn.disabled = true; // Vô hiệu hóa nút gửi
        document.querySelectorAll(".otp-input input").forEach(input => input.disabled = true); // Vô hiệu hóa các ô nhập
      }
    }, 1000); // 1 giây xử lý một lần
  }

  // Xử lý sự kiện "Gửi lại OTP"
  resendLink.addEventListener("click", function (e) {
    const now = new Date().getTime();

    if (now < expireTime) {
      // Nếu thời gian chưa hết
      e.preventDefault(); // Ngăn nút "Gửi lại OTP"
      alert("Bạn phải chờ hết thời gian để gửi lại OTP."); // Hiển thị cảnh báo
    } else {
      // Nếu hết thời gian, đặt lại thời gian mới
      expireTime = now + OTP_DURATION * 1000; // Đặt 120 giây mới
      localStorage.setItem(STORAGE_KEY, expireTime); // Lưu vào localStorage
      startCountdown(); // Khởi động lại bộ đếm
    }
  });

  // Hàm khởi tạo phụ trách việc kiểm tra và thiết lập initial state
  (function initializeTimer() {
    const prevEmail = localStorage.getItem("current_email"); // Lấy email tài khoản trước

    // Nếu người dùng đổi email
    if (prevEmail && prevEmail !== email) {
      cleanExpiredTimer(prevEmail); // Xóa bộ đếm thời gian cũ
    }

    // Lưu email hiện tại vào localStorage
    localStorage.setItem("current_email", email);

    // Bắt đầu đếm thời gian cho email hiện tại
    startCountdown();
  })();
</script>
<script>
  function move(current, nextId) {
    if (current.value.length === 1) {
      document.getElementsByName(nextId)[0]?.focus();
    }
  }

  function submitOtp() {
    const inputs = document.querySelectorAll('.otp-input input');
    let code = '';
    for (let input of inputs) {
      if (!input.value) return false;
      code += input.value;
    }
    document.getElementById('otp').value = code;
    return true;
  }

  function confirmBack() {
  // Hiển thị hộp thoại xác nhận
  const userConfirm = confirm("Bạn có chắc chắn muốn quay lại trang đăng nhập không?");
  
  // Nếu người dùng xác nhận quay lại
  if (userConfirm) {
    return true; // Cho phép điều hướng đến URL của nút
  }

  // Nếu người dùng hủy, chặn hành động điều hướng
  return false;
}

  const OTP_DURATION = 120; // 120 giây đếm ngược
const timerDisplay = document.getElementById("timer"); // Thẻ hiển thị thời gian
const submitBtn = document.getElementById("submitBtn"); // Nút gửi OTP
const resendLink = document.getElementById("resendLink"); // Nút gửi lại OTP

// Lấy email từ session, đây là email đang sử dụng tài khoản hiện tại
const email = `<%= session.getAttribute("email") %>`; 
const STORAGE_KEY = `otp_expire_time_${email}`; // Tạo khóa duy nhất cho tài khoản hiện tại
let expireTime;

// Hàm xóa dữ liệu cũ khi đổi tài khoản
function cleanExpiredTimer(prevEmail) {
  const oldKey = `otp_expire_time_${prevEmail}`;
  if (localStorage.getItem(oldKey)) {
    localStorage.removeItem(oldKey); // Xóa dữ liệu của tài khoản cũ
  }
}

// Lấy thông tin expireTime cho tài khoản hiện tại
function getExpireTime() {
  const currentTime = new Date().getTime();
  let storedTime = parseInt(localStorage.getItem(STORAGE_KEY)); // Lấy expireTime từ localStorage

  // Nếu không tồn tại hoặc hết hạn, khởi tạo thời gian mới
  if (!storedTime || currentTime >= storedTime) {
    storedTime = currentTime + OTP_DURATION * 1000; // Thiết lập 120s mới
    localStorage.setItem(STORAGE_KEY, storedTime); // Cập nhật localStorage
  }
  return storedTime;
}

// Khởi động đếm ngược
function startCountdown() {
  expireTime = getExpireTime(); // Lấy giá trị expireTime cho tài khoản hiện tại
  const countdown = setInterval(() => {
    const now = new Date().getTime();
    const timeLeft = Math.max(0, Math.ceil((expireTime - now) / 1000));

    timerDisplay.textContent = timeLeft; // Cập nhật UI hiển thị thời gian

    if (timeLeft <= 0) {
      // Khi hết thời gian
      clearInterval(countdown); // Dừng bộ đếm
      timerDisplay.textContent = "0"; // Hiển thị "0"
      submitBtn.disabled = true; // Vô hiệu hóa nút gửi
      document.querySelectorAll(".otp-input input").forEach(input => input.disabled = true); // Vô hiệu hóa các ô nhập
    }
  }, 1000); // Lặp lại mỗi giây
}

// Xử lý sự kiện "gửi lại OTP"
resendLink.addEventListener("click", function (e) {
  const now = new Date().getTime();

  if (now < expireTime) {
    // Nếu thời gian chưa hết
    e.preventDefault(); // Ngăn hành động gửi lại OTP
    alert("Bạn chỉ có thể gửi lại mã OTP sau khi hết thời gian chờ!"); // Hiển thị cảnh báo cho người dùng
  } else {
    // Nếu hết thời gian, thiết lập lại
    expireTime = now + OTP_DURATION * 1000; // Cộng thêm 120 giây mới
    localStorage.setItem(STORAGE_KEY, expireTime); // Lưu giá trị mới vào localStorage
    startCountdown(); // Khởi động lại bộ đếm
  }
});

// Gọi hàm cleanExpiredTimer khi trang được tải
(function initializeTimer() {
  const prevEmail = localStorage.getItem("current_email"); // Tài khoản cũ nếu có

  // Nếu tài khoản cũ khác với tài khoản hiện tại, xóa dữ liệu cũ
  if (prevEmail && prevEmail !== email) {
    cleanExpiredTimer(prevEmail); // Xóa thông tin của tài khoản trước
  }

  // Cập nhật tài khoản hiện tại vào localStorage
  localStorage.setItem("current_email", email);

  // Khởi động đếm ngược cho tài khoản hiện tại
  startCountdown();
})();
</script>