
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.AccountStaff" %>

<%
  AccountStaff staff = (AccountStaff) session.getAttribute("account");
  int staffId = (staff != null) ? staff.getAccount_staff_id() : 0;
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Hồ sơ nhân viên</title>
  <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 p-10">

<div class="max-w-3xl mx-auto bg-white p-6 rounded-xl shadow-xl">
  <h2 class="text-3xl font-bold text-blue-700 mb-6">Hồ sơ cá nhân</h2>

  <div id="profileInfo" class="space-y-4 text-lg text-gray-700">
    <p>Đang tải thông tin...</p>
  </div>

  <div class="mt-6 text-right">
    <a href="<%= request.getContextPath() %>/view/accountpharmacist/ChangePassword.jsp"
       class="inline-block bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded-lg shadow transition duration-200">
      Đổi mật khẩu
    </a>
  </div>
</div>


<script>
  const staffId = <%= staffId %>;

  if (!staffId || staffId === 0) {
    document.getElementById("profileInfo").innerHTML =
            "<p class='text-red-600'>Không tìm thấy thông tin nhân viên. Vui lòng đăng nhập lại.</p>";
  } else {
    axios.get("<%= request.getContextPath() %>/api/staff/profile?staffId=" + staffId)
            .then(res => {
              const profile = res.data;

              // Hàm kiểm tra dữ liệu an toàn
              function safe(value) {
                return (value !== null && value !== undefined && value !== "" && value !== "false") ? value : "N/A";
              }

              // Xử lý thông tin bổ sung nếu là bác sĩ
              let extraInfo = "";
              if (profile.role === 'Doctor') {
                extraInfo += "<p><strong>Trình độ học vấn:</strong> " + safe(profile.eduLevel) + "</p>";
                extraInfo += "<p><strong>Tình trạng làm việc:</strong> " + safe(profile.availability) + "</p>";
              }

              // Render toàn bộ HTML thông tin
              const infoHtml =
                      "<p><strong>Họ tên:</strong> " + safe(profile.fullName) + "</p>" +
                      "<p><strong>Email:</strong> " + safe(profile.email) + "</p>" +
                      "<p><strong>Vai trò:</strong> " + safe(profile.role) + "</p>" +
                      "<p><strong>Số điện thoại:</strong> " + safe(profile.phone) + "</p>" +
                      "<p><strong>Phòng ban:</strong> " + safe(profile.department) + "</p>" +
                      extraInfo +
                      "<p><strong>Trạng thái:</strong> " + safe(profile.status) + "</p>";

              document.getElementById("profileInfo").innerHTML = infoHtml;
            })
            .catch(err => {
              console.error("Lỗi khi gọi API hồ sơ:", err);
              document.getElementById("profileInfo").innerHTML =
                      "<p class='text-red-600'>Lỗi khi tải dữ liệu hồ sơ. Vui lòng thử lại sau.</p>";
            });
  }


</script>
</body>
</html>
