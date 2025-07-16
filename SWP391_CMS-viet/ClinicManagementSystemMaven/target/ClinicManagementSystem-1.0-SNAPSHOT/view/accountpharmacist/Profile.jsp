<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Trang Cá Nhân</title>
    <style>
        body {
            margin: 0;
            font-family: 'Segoe UI', sans-serif;
            background: linear-gradient(to right, #d0f0fd, #f0faff);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .profile-box {
            background-color: #fff;
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
            width: 360px;
            text-align: center;
        }

        .avatar {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            object-fit: cover;
            margin-bottom: 20px;
            border: 3px solid #00acc1;
        }

        h2 {
            margin-bottom: 10px;
            color: #007c91;
        }

        .info {
            font-size: 15px;
            margin: 8px 0;
            color: #333;
        }

        .status {
            background: #e0f7fa;
            color: #00796b;
            display: inline-block;
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 13px;
            margin-top: 12px;
        }

        .btn-edit {
            margin-top: 24px;
            background: #00acc1;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            font-size: 15px;
            cursor: pointer;
            transition: 0.3s;
        }

        .btn-edit:hover {
            background: #008b9a;
        }
    </style>
</head>
<body>
<div class="profile-box">
    <img src="https://i.imgur.com/BoN9kdC.png" alt="Avatar" class="avatar">
    <h2>Nguyễn Văn A</h2>
    <div class="info">📧 Email: nguyenvana@gmail.com</div>
    <div class="info">👤 Tên đăng nhập: nguyenvana</div>
    <div class="info">🔒 Trạng thái: <span class="status">Đang hoạt động</span></div>

    <button class="btn-edit">✏️ Chỉnh sửa thông tin</button>
</div>
</body>
</html>
