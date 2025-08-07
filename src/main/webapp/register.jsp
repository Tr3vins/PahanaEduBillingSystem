<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="login-page">
<div class="login-container">
    <h2 class="login-title">Pahana Edu Billing System</h2>
    <h3 class="login-subtitle">Register</h3>

    <% if (request.getAttribute("errorMessage") != null) { %>
    <p class="error-message"><%= request.getAttribute("errorMessage") %></p>
    <% } %>

    <form action="register" method="post">
        <div class="form-group">
            <input type="text" id="username" name="username" placeholder="Username" class="form-input" required>
        </div>
        <div class="form-group">
            <input type="password" id="password" name="password" placeholder="Password" class="form-input" required>
        </div>
        <div class="form-group">
            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm Password" class="form-input" required>
        </div>
        <button type="submit" class="form-button">Register</button>
    </form>
    <p class="login-text">
        Already have an account? <a href="login.jsp" class="login-link">Login</a>
    </p>
</div>
</body>
</html>
