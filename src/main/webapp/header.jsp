<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Pahana Edu Billing System</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<c:if test="${sessionScope.currentUser == null}">
  <c:redirect url="login.jsp"/>
</c:if>

<div class="top-bar">
  <div class="header">
    <a href="dashboard.jsp">
      <h1>Pahana Edu Billing System</h1>
    </a>
    <form action="logout" method="get">
      <button type="submit" class="logout-button">Logout</button>
    </form>
  </div>
  <nav class="main-nav">
    <a href="dashboard.jsp" class="nav-link">
      <i class="fas fa-home"></i>Dashboard
    </a>
    <a href="customer" class="nav-link">
      <i class="fas fa-users"></i>Customers
    </a>
    <a href="item" class="nav-link">
      <i class="fas fa-box"></i>Items
    </a>
    <a href="bill" class="nav-link">
      <i class="fas fa-file-invoice"></i>Create Bill
    </a>
    <a href="help.jsp" class="nav-link">
      <i class="fas fa-question-circle"></i>Help
    </a>
  </nav>
</div>
</body>
</html>