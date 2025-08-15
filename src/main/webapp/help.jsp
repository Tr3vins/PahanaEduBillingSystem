<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Help</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<c:if test="${sessionScope.currentUser == null}">
    <c:redirect url="login.jsp"/>
</c:if>

<%@ include file="header.jsp" %>

<!-- Main Content -->
<div class="main-content">
    <div class="title-text">
        <h2>Help & Usage Guidelines</h2>
    </div>
    <p class="sub-text">Welcome to the Pahana Edu Billing System! This guide will walk you through the key features to help you get started.</p>

    <div class="help-container">
        <!-- Dashboard Section -->
        <div class="help-card">
            <h3 class="help-title">
                <i class="fas fa-tachometer-alt mr-3 icon-help"></i>Dashboard
            </h3>
            <p class="help-description">
                The Dashboard is your main hub. From here, you can quickly navigate to all the core functions of the billing system using the intuitive tile-based layout.
            </p>
        </div>

        <!-- Manage Customers Section -->
        <div class="help-card">
            <h3 class="help-title">
                <i class="fas fa-users mr-3 icon-customer"></i>Manage Customers
            </h3>
            <p class="help-description">
                This section allows you to manage all customer information.
            </p>
            <ul class="help-list">
                <li><strong>Add New Customer:</strong> Click the <strong><span style="color: #feb47b;">Add New Customer</span></strong> button, fill out the required details (Account Number, Name, Address, Telephone), and save.</li>
                <li><strong>Edit Customer:</strong> Click the <strong><span style="color: #60a5fa;">Edit</span></strong> button next to a customer's entry. The form will be pre-populated with their current information, which you can modify before saving.</li>
                <li><strong>Delete Customer:</strong> Click the <strong><span style="color: #ef4444;">Delete</span></strong> button. A confirmation modal will appear to ensure you don't accidentally delete a customer.</li>
            </ul>
        </div>

        <!-- Manage Items Section -->
        <div class="help-card">
            <h3 class="help-title">
                <i class="fas fa-box-open mr-3 icon-item"></i>Manage Items
            </h3>
            <p class="help-description">
                Here you can manage the products you sell, including their prices and stock levels.
            </p>
            <ul class="help-list">
                <li><strong>Add New Item:</strong> Click the <strong><span style="color: #feb47b;">Add New Item</span></strong> button to create a new product, setting its Name, Unit Price, and initial Stock Quantity.</li>
                <li><strong>Edit Item:</strong> Click the <strong><span style="color: #60a5fa;">Edit</span></strong> button to update an item's details.</li>
                <li><strong>Delete Item:</strong> Click the <strong><span style="color: #ef4444;">Delete</span></strong> button to remove an item from your inventory. A confirmation will be required.</li>
            </ul>
        </div>

        <!-- Create Bill Section -->
        <div class="help-card">
            <h3 class="help-title">
                <i class="fas fa-file-invoice-dollar mr-3 icon-bill"></i>Create Bill
            </h3>
            <p class="help-description">
                This is where you can generate new bills for your customers.
            </p>
            <ul class="help-list">
                <li><strong>Select Customer:</strong> Choose a customer from the dropdown list.</li>
                <li><strong>Add Items:</strong> Select an item and a quantity, then click the <strong><span style="color: #feb47b;">Add Item</span></strong> button. The item will appear in the bill summary table.</li>
                <li><strong>Generate Bill:</strong> Once all items are added, click <strong><span style="color: #feb47b;">Create Bill</span></strong> to finalize the transaction. The system will then generate a bill for you.</li>
                <li><strong>Print Bill:</strong> Use the <strong><span style="color: #feb47b;">Print</span></strong> button to print the bill.</li>
            </ul>
        </div>
    </div>
</div>

</body>
</html>
