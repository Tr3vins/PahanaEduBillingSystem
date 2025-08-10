<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Bill</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<c:if test="${sessionScope.currentUser == null}">
  <c:redirect url="login.jsp"/>
</c:if>

<%@ include file="header.jsp" %>

<div class="main-content">

  <c:if test="${not empty requestScope.successMessage}">
    <div class="success-message">
      <c:out value="${requestScope.successMessage}"/>
    </div>
  </c:if>
  <c:if test="${not empty requestScope.errorMessage}">
    <div class="error-message">
      <c:out value="${requestScope.errorMessage}"/>
    </div>
  </c:if>

  <div class="flex justify-end p-4 print-button-container">
    <button onclick="window.print()" class="add-button">
      <i class="fas fa-print mr-2"></i> Print
    </button>
  </div>

  <div class="bill-container">
    <div class="bill-header">
      <div>
        <h1 class="bill-title">Bill</h1>
        <p class="bill-company-info">Pahana Edu</p>
        <p class="bill-company-info">123 Business Lane, Colombo</p>
      </div>
      <div>
        <p class="bill-text"><strong>Bill #:</strong> ${bill.billId}</p>
        <p class="bill-text"><strong>Date:</strong> <fmt:formatDate value="${bill.billDate}" pattern="yyyy-MM-dd" /></p>
      </div>
    </div>

    <div class="bill-grid">
      <div>
        <h4 class="bill-section-title">Bill To:</h4>
        <p class="bill-text"><strong>Account Number:</strong> ${customer.accountNumber}</p>
        <p class="bill-text"><strong>Name:</strong> ${customer.name}</p>
        <p class="bill-text"><strong>Address:</strong> ${customer.address}</p>
        <p class="bill-text"><strong>Telephone:</strong> ${customer.telephoneNumber}</p>
      </div>
    </div>


    <h4 class="bill-section-title">Items Billed</h4>
    <div class="table-container">
      <table class="data-table">
        <thead>
        <tr>
          <th>Item Name</th>
          <th>Unit Price</th>
          <th>Quantity</th>
          <th>Sub Total</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${bill.billItems}">
          <tr>
            <td>${item.itemName}</td>
            <td>
              <fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="Rs."/>
            </td>
            <td>${item.quantity}</td>
            <td>
              <fmt:formatNumber value="${item.subTotal}" type="currency" currencySymbol="Rs."/>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>


    <div class="bill-total-amount">
      <p>Total:</p>
      <span><fmt:formatNumber value="${bill.totalAmount}" type="currency" currencySymbol="Rs."/></span>
    </div>

    <div class="bill-footer">
      <p>Thank you for your business!</p>
    </div>
  </div>
</div>
</body>
</html>
