<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Customers</title>
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
        <h2>Manage Customers</h2>
    </div>
    <p class="sub-text">Select an option below to manage your Customers:</p>

    <!-- Display Messages -->
    <% if (request.getAttribute("errorMessage") != null) { %>
    <p class="error-message"><%= request.getAttribute("errorMessage") %></p>
    <% } %>
    <% if (request.getAttribute("successMessage") != null) { %>
    <p class="success-message"><%= request.getAttribute("successMessage") %></p>
    <% } %>

    <!-- Add Customer Button -->
    <div class="flex justify-end mb-6">
        <button id="addCustomerBtn" class="add-button">
            <i class="fas fa-plus mr-2"></i>Add New Customer
        </button>
    </div>

    <!-- Customer Table -->
    <div class="table-container">
        <table class="data-table">
            <thead>
            <tr>
                <th>Account Number</th>
                <th>Name</th>
                <th>Address</th>
                <th>Telephone</th>
                <th>Registration Date</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="customer" items="${listCustomers}">
                <tr class="hover:bg-gray-50">
                    <td><c:out value="${customer.accountNumber}"/></td>
                    <td><c:out value="${customer.name}"/></td>
                    <td><c:out value="${customer.address}"/></td>
                    <td><c:out value="${customer.telephoneNumber}"/></td>
                    <td><c:out value="${customer.registrationDate}"/></td>
                    <td class="whitespace-nowrap">
                        <button class="edit-button"
                                data-account-number="<c:out value="${customer.accountNumber}"/>"
                                data-name="<c:out value="${customer.name}"/>"
                                data-address="<c:out value="${customer.address}"/>"
                                data-telephone="<c:out value="${customer.telephoneNumber}"/>">Edit</button>
                        <button class="delete-button"
                                data-account-number="<c:out value="${customer.accountNumber}"/>">Delete</button>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty listCustomers}">
                <tr>
                    <td colspan="6" class="data-table-empty-text">No customers found.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>
