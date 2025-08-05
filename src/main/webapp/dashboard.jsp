<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<c:if test="${sessionScope.currentUser == null}">
    <c:redirect url="login.jsp"/>
</c:if>

<div class="header">
    <h1>Pahana Edu Billing System</h1>
    <form action="logout" method="get">
        <button type="submit" class="logout-button">Logout</button>
    </form>
</div>

<div class="main-content">
    <div class="title-text">
        <h2>Dashboard</h2>
        <p class="welcome-message">Welcome, <c:out value="${sessionScope.currentUser.username}" />!</p>
    </div>
    <p class="sub-text">Select an option below to manage your billing system:</p>

    <div class="tiles-grid">
        <a href="${pageContext.request.contextPath}/customer" class="tile-card">
            <i class="fas fa-users tile-icon icon-customer"></i>
            <h3 class="tile-title">Manage Customers</h3>
            <p class="tile-description">Add, edit, or remove customers.</p>
        </a>

        <a href="${pageContext.request.contextPath}/item" class="tile-card">
            <i class="fas fa-box-open tile-icon icon-item"></i>
            <h3 class="tile-title">Manage Items</h3>
            <p class="tile-description">Manage products, their prices, and stock levels.</p>
        </a>

        <a href="${pageContext.request.contextPath}/bill" class="tile-card">
            <i class="fas fa-file-invoice-dollar tile-icon icon-bill"></i>
            <h3 class="tile-title">Create Bill</h3>
            <p class="tile-description">Generate a new bill for a customer.</p>
        </a>

        <a href="${pageContext.request.contextPath}/help" class="tile-card">
            <i class="fas fa-circle-question tile-icon icon-help"></i>
            <h3 class="tile-title">Help</h3>
            <p class="tile-description">System usage guidelines.</p>
        </a>
    </div>
</div>
</body>
</html>
