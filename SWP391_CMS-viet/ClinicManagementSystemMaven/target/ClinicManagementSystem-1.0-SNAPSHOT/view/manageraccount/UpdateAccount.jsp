<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="dao.PharmacistDAO" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.SQLException" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Pharmacist Account</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
        }
        input, select {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .error {
            color: red;
            margin-bottom: 10px;
        }
        .success {
            color: green;
            margin-bottom: 10px;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<h2>Update Pharmacist Account</h2>

<div id="message"></div>

<%
    String error = request.getParameter("error");
    if (error != null) {
        out.println("<p class='error'>Error: " + error + "</p>");
    }

    int pharmacistId;
    try {
        pharmacistId = Integer.parseInt(request.getParameter("pharmacistId"));
    } catch (NumberFormatException e) {
        out.println("<p class='error'>Invalid Pharmacist ID</p>");
        return;
    }

    Connection conn = null;
    ResultSet rs = null;
    try {
        conn = dao.DBContext.getInstance().getConnection();
        PharmacistDAO dao = new PharmacistDAO(conn);
        rs = dao.getPharmacistById(pharmacistId);
        if (rs.next()) {
%>
<form id="updateForm">
    <input type="hidden" name="pharmacistId" id="pharmacistId" value="<%= rs.getInt("pharmacist_id") %>">
    <input type="hidden" name="accountPharmacistId" id="accountPharmacistId" value="<%= rs.getInt("account_pharmacist_id") %>">
    <input type="hidden" name="action" id="action" value="update">

    <div class="form-group">
        <label for="fullName">Full Name:</label>
        <input type="text" id="fullName" name="name" value="<%= rs.getString("full_name") %>" required>
    </div>

    <div class="form-group">
        <label for="phone">Phone:</label>
        <input type="text" id="phone" name="mobile" value="<%= rs.getString("phone") %>" required pattern="[0-9]{10,11}" title="Phone number must be 10 or 11 digits">
    </div>

    <div class="form-group">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" value="<%= rs.getString("username") %>" required>
    </div>

    <div class="form-group">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" value="<%= rs.getString("email") %>" required>
    </div>

    <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" value="<%= rs.getString("password") %>" required>
    </div>

    <div class="form-group">
        <label for="status">Status:</label>
        <select id="status" name="status" required>
            <option value="Enable" <%= rs.getString("status").equals("Enable") ? "selected" : "" %>>Enable</option>
            <option value="Disable" <%= rs.getString("status").equals("Disable") ? "selected" : "" %>>Disable</option>
        </select>
    </div>

    <div class="form-group">
        <label for="img">Image URL:</label>
        <input type="url" id="img" name="img" value="<%= rs.getString("img") %>" required>
    </div>

    <button type="submit">Update Pharmacist</button>
</form>
<%
        } else {
            out.println("<p class='error'>Pharmacist not found!</p>");
        }
    } catch (SQLException e) {
        out.println("<p class='error'>Error retrieving pharmacist: " + e.getMessage() + "</p>");
    } finally {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                out.println("<p class='error'>Error closing ResultSet: " + e.getMessage() + "</p>");
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                out.println("<p class='error'>Error closing connection: " + e.getMessage() + "</p>");
            }
        }
    }
%>

<script>
    document.getElementById('updateForm').addEventListener('submit', async function(event) {
        event.preventDefault();

        const form = event.target;
        const formData = new FormData(form);
        const data = Object.fromEntries(formData);

        try {
            const response = await fetch('/api/account', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(data)
            });

            const result = await response.json();
            const messageDiv = document.getElementById('message');

            if (response.ok) {
                messageDiv.className = 'success';
                messageDiv.textContent = result || 'Account updated successfully';
            } else {
                messageDiv.className = 'error';
                messageDiv.textContent = result.error || 'Failed to update account';
            }
        } catch (error) {
            document.getElementById('message').className = 'error';
            document.getElementById('message').textContent = 'Error: ' + error.message;
        }
    });
</script>
</body>
</html>