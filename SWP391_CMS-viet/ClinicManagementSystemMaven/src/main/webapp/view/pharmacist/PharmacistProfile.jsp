<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.AccountPharmacist" %>

<%
  AccountPharmacist pharmacist = (AccountPharmacist) session.getAttribute("account");
  int pharmacistId = (pharmacist != null) ? pharmacist.getAccount_pharmacist_id() : 0;
%>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Hồ sơ Dược sĩ</title>
  <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 p-10">

<div class="max-w-2xl mx-auto bg-white p-6 rounded-xl shadow-lg">
  <h2 class="text-3xl font-bold text-green-700 mb-6">Hồ sơ Dược sĩ</h2>

  <div id="profileContainer" class="flex items-center space-x-6 mb-6">
    <img id="profileImage" src="" alt="Ảnh đại diện" class="w-28 h-28 rounded-full object-cover border">
    <div>
      <h3 id="profileName" class="text-2xl font-semibold text-gray-800">Đang tải...</h3>
      <p id="profileEmail" class="text-gray-600">...</p>
      <p id="profilePhone" class="text-gray-600">...</p>
    </div>
  </div>


</div>

<script>
  const pharmacistId = <%= pharmacistId %>;

  if (!pharmacistId || pharmacistId === 0) {
    document.getElementById("profileContainer").innerHTML =
            "<p class='text-red-600'>Không tìm thấy thông tin. Vui lòng đăng nhập lại.</p>";
  } else {
    axios.get("<%= request.getContextPath() %>/api/pharmacist/profile?pharmacistId=" + pharmacistId)
            .then(res => {
              const profile = res.data;

              function safe(val) {
                return (val !== null && val !== undefined && val !== "") ? val : "N/A";
              }

              document.getElementById("profileName").textContent = safe(profile.name);     // sửa ở đây
              document.getElementById("profileEmail").textContent = "📧 " + safe(profile.email);
              document.getElementById("profilePhone").textContent = "📞 " + safe(profile.mobile);  // sửa ở đây
              document.getElementById("profileImage").src = safe(profile.img);
            })
            .catch(err => {
              console.error(err);
              document.getElementById("profileContainer").innerHTML =
                      "<p class='text-red-600'>Không thể tải hồ sơ. Vui lòng thử lại sau.</p>";
            });

  }
</script>

</body>
</html>
