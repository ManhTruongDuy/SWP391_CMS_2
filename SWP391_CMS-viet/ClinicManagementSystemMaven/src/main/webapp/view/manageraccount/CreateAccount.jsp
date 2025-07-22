<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Account</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: inline-block; width: 100px; }
        input { padding: 5px; }
        #result { margin-top: 20px; padding: 10px; border: 1px solid #ccc; }
    </style>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function() {
            $("#create").click(function() {
                let account = {
                    name: $("#name").val(),
                    mobile: $("#mobile").val(),
                    email: $("#email").val(),
                    password: $("#password").val()
                };
                // Sử dụng context path động
                let contextPath = "<%=request.getContextPath()%>";
                $.ajax({
                    url: contextPath + "/api/account/",
                    type: "POST",
                    contentType: "application/json",
                    data: JSON.stringify(account),
                    success: function(data, status) {
                        $("#result").html("<pre>" + JSON.stringify(data, null, 2) + "</pre>");
                        $("#result").css("background-color", "#e8f5e9");
                    },
                    error: function(xhr, status, error) {
                        let errorMsg = "Lỗi: " + status + " - " + error + "\n" + JSON.stringify(JSON.parse(xhr.responseText), null, 2);
                        $("#result").html("<pre>" + errorMsg + "</pre>");
                        $("#result").css("background-color", "#ffebee");
                        console.log("Chi tiết lỗi:", xhr.responseText);
                    }
                });
            });
        });
    </script>
</head>
<body>
<h2>Create Account</h2>

<div class="form-group">
    <label>Name:</label><input type="text" id="name" placeholder="Enter name" required><br>
</div>
<div class="form-group">
    <label>Mobile:</label><input type="text" id="mobile" placeholder="(123)-456-7890" required><br>
</div>
<div class="form-group">
    <label>Email:</label><input type="email" id="email" placeholder="Enter email" required><br>
</div>
<div class="form-group">
    <label>Password:</label><input type="password" id="password" placeholder="Enter password" required><br>
</div>

<button id="create">Create Account</button>
<div id="result">Result will appear here...</div>
</body>
</html>