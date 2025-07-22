<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Xóa các giá trị cũ khỏi session
    session.removeAttribute("otp_created_time");
    session.removeAttribute("otp");
    session.removeAttribute("email");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PharmaCare | Pharmacy Management System</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .pharmacy-bg {
            background: linear-gradient(135deg, rgba(0, 119, 182, 0.9) 0%, rgba(0, 180, 216, 0.8) 100%), url('https://images.unsplash.com/photo-1579684385127-1ef15d508118?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80') no-repeat center center;
            background-size: cover;
        }
        .input-field:focus {
            box-shadow: 0 0 0 3px rgba(0, 180, 216, 0.3);
        }
        .pill-icon {
            animation: float 3s ease-in-out infinite;
        }
        @keyframes float {
            0% { transform: translateY(0px); }
            50% { transform: translateY(-10px); }
            100% { transform: translateY(0px); }
        }
    </style>
</head>
<body class="bg-gray-100 min-h-screen flex items-center justify-center p-4">
<div class="max-w-6xl w-full flex rounded-2xl shadow-2xl overflow-hidden">
    <!-- Left Side - Pharmacy Image -->
    <div class="hidden md:flex md:w-1/2 pharmacy-bg text-white p-12 flex-col justify-between">
        <div>
            <h1 class="text-4xl font-bold mb-2">PharmaCare</h1>
            <p class="text-xl opacity-90">Advanced Pharmacy Management System</p>
        </div>

        <div class="mt-8">
            <div class="flex items-center mb-6">
                <div class="pill-icon mr-4">
                    <i class="fas fa-pills text-4xl"></i>
                </div>
                <div>
                    <h3 class="text-xl font-semibold">Inventory Tracking</h3>
                    <p class="opacity-80">Real-time medication stock management</p>
                </div>
            </div>

            <div class="flex items-center mb-6">
                <div class="pill-icon mr-4">
                    <i class="fas fa-user-md text-4xl"></i>
                </div>
                <div>
                    <h3 class="text-xl font-semibold">Patient Records</h3>
                    <p class="opacity-80">Comprehensive patient medication history</p>
                </div>
            </div>

            <div class="flex items-center">
                <div class="pill-icon mr-4">
                    <i class="fas fa-file-prescription text-4xl"></i>
                </div>
                <div>
                    <h3 class="text-xl font-semibold">e-Prescriptions</h3>
                    <p class="opacity-80">Digital prescription processing</p>
                </div>
            </div>
        </div>

        <div class="text-sm opacity-80">
            <p>© 2023 PharmaCare Management System. All rights reserved.</p>
        </div>
    </div>

    <!-- Right Side - Login Form -->
    <div class="w-full md:w-1/2 bg-white p-8 md:p-12 flex flex-col justify-center">
        <div class="text-center mb-8 md:hidden">
            <i class="fas fa-pills text-5xl text-blue-500 mb-2"></i>
            <h1 class="text-3xl font-bold text-gray-800">PharmaCare</h1>
            <p class="text-gray-600">Pharmacy Management System</p>
        </div>

        <h2 class="text-2xl font-bold text-gray-800 mb-1">Welcome Back</h2>
        <p class="text-gray-600 mb-8">Sign in to your pharmacy dashboard</p>

        <form action="<%=request.getContextPath()%>/loginservlet" method="POST" class="space-y-6">


            <div>
                <label for="email" class="block text-sm font-medium text-gray-700 mb-1">Gmail</label>
                <div class="relative">
                    <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                        <i class="fas  fa-user text-gray-400"></i>
                    </div>
                    <input type="text" id="email" name="email"
                           class="input-field pl-10 w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:border-blue-500 transition"
                           placeholder="Nhập tên đăng nhập" required
                           oninvalid="this.setCustomValidity('Vui lòng nhập email')"
                           oninput="this.setCustomValidity('')">
                </div>
            </div>

            <div>
                <label for="password" class="block text-sm font-medium text-gray-700 mb-1">Password</label>
                <div class="relative">
                    <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                        <i class="fas fa-lock text-gray-400"></i>
                    </div>
                    <input type="password" id="password" name="password"
                           class="input-field pl-10 w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:border-blue-500 transition"
                           placeholder="Nhập mật khẩu" required
                           oninvalid="this.setCustomValidity('Vui lòng nhập mật khẩu')"
                           oninput="this.setCustomValidity('')">
                    <div class="absolute inset-y-0 right-0 pr-3 flex items-center cursor-pointer" onclick="togglePassword()">
                        <i id="eyeIcon" class="fas fa-eye-slash text-gray-400"></i>
                    </div>
                </div>
            </div>

            <div class="flex items-center justify-between">
                <div class="flex items-center">
                    <input id="remember-me" name="remember-me" type="checkbox"
                           class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded">
                    <label for="remember-me" class="ml-2 block text-sm text-gray-700">Remember me</label>
                </div>

                <div class="text-sm">
                    <a href="<%=request.getContextPath()%>/view/accountpharmacist/ForgotPassword.jsp"
                       class="font-medium text-blue-600 hover:text-blue-500">Quên mật khẩu?</a>

                </div>
            </div>

            <button type="submit"
                    class="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-3 px-4 rounded-lg transition duration-300 flex items-center justify-center">
                <span>Sign In</span>
                <i class="fas fa-arrow-right ml-2"></i>
            </button>
            <p class="text-red-600 text-sm text-center mt-2">${err}</p>


        </form>



    </div>
</div>

<script>

    function togglePassword() {
        const passwordInput = document.getElementById('password');
        const eyeIcon = document.getElementById('eyeIcon');

        if (passwordInput.type === 'password') {
            passwordInput.type = 'text';
            eyeIcon.classList.remove('fa-eye-slash');
            eyeIcon.classList.add('fa-eye');
        } else {
            passwordInput.type = 'password';
            eyeIcon.classList.remove('fa-eye');
            eyeIcon.classList.add('fa-eye-slash');
        }
    }



</script>
</body>
</html>