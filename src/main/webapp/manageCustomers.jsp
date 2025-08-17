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

<%@ include file="header.jsp" %>

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

<!-- Add/Edit Customer Modal -->
<div id="customerModal" class="modal-overlay">
    <div class="modal-content">
        <button class="modal-close-button" id="closeModalBtn">&times;</button>
        <h2 id="modalTitle" class="modal-title"></h2>
        <form id="customerForm" action="customer" method="post">
            <input type="hidden" name="action" id="formAction">

            <div class="form-group">
                <label for="accountNumber" class="form-label">Account Number:</label>
                <input type="text" id="accountNumber" name="accountNumber" class="form-input" required>
            </div>

            <div class="form-group">
                <label for="name" class="form-label">Customer Name:</label>
                <input type="text" id="name" name="name" class="form-input" required>
            </div>

            <div class="form-group">
                <label for="address" class="form-label">Address:</label>
                <input type="text" id="address" name="address" class="form-input">
            </div>

            <div class="form-group">
                <label for="telephoneNumber" class="form-label">Telephone Number:</label>
                <input type="number" id="telephoneNumber" name="telephoneNumber" class="form-input" required>
            </div>

            <button type="submit" id="submitButton" class="form-button"></button>
        </form>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div id="deleteModal" class="modal-overlay">
    <div class="modal-content">
        <h2 class="modal-title">Confirm Deletion</h2>
        <p class="text-center text-gray-100">Are you sure you want to delete the customer: <span id="deleteAccountNumber" class="font-bold"></span>?</p>
        <form id="deleteForm" action="customer" method="post" class="modal-container">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="accountNumber" id="deleteFormAccountNumber">
            <button type="button" id="cancelDeleteBtn" class="modal-cancel-button">Cancel</button>
            <button type="submit" class="modal-delete-button">Delete</button>
        </form>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', () => {
        const addCustomerBtn = document.getElementById('addCustomerBtn');
        const customerModal = document.getElementById('customerModal');
        const deleteModal = document.getElementById('deleteModal');
        const closeModalBtn = document.getElementById('closeModalBtn');
        const cancelDeleteBtn = document.getElementById('cancelDeleteBtn');
        const formAction = document.getElementById('formAction');
        const modalTitle = document.getElementById('modalTitle');
        const submitButton = document.getElementById('submitButton');
        const customerForm = document.getElementById('customerForm');
        const accountNumberInput = document.getElementById('accountNumber');
        const nameInput = document.getElementById('name');
        const addressInput = document.getElementById('address');
        const telephoneInput = document.getElementById('telephoneNumber');
        const deleteAccountNumberSpan = document.getElementById('deleteAccountNumber');
        const deleteFormAccountNumberInput = document.getElementById('deleteFormAccountNumber');

        // Function to open the modal
        const openModal = (modal) => {
            modal.classList.add('show');
        };

        // Function to close the modal
        const closeModal = (modal) => {
            modal.classList.remove('show');
        };

        // Open Add Customer Modal
        addCustomerBtn.addEventListener('click', () => {
            modalTitle.textContent = 'Add New Customer';
            formAction.value = 'add';
            submitButton.textContent = 'Add Customer';
            customerForm.reset();
            accountNumberInput.readOnly = false;
            // Remove disabled styling
            accountNumberInput.classList.remove('bg-gray-700', 'text-gray-400', 'cursor-not-allowed');
            openModal(customerModal);
        });

        // Open Edit Customer Modal
        document.querySelectorAll('.edit-button').forEach(button => {
            button.addEventListener('click', (e) => {
                const accountNumber = e.target.getAttribute('data-account-number');
                const name = e.target.getAttribute('data-name');
                const address = e.target.getAttribute('data-address');
                const telephone = e.target.getAttribute('data-telephone');

                modalTitle.textContent = 'Edit Customer';
                formAction.value = 'update';
                submitButton.textContent = 'Update Customer';

                accountNumberInput.value = accountNumber;
                nameInput.value = name;
                addressInput.value = address;
                telephoneInput.value = telephone;

                // Set account number to read-only for editing
                accountNumberInput.readOnly = true;
                // Add disabled styling for dark theme
                accountNumberInput.classList.add('bg-gray-700', 'text-gray-400', 'cursor-not-allowed');
                openModal(customerModal);
            });
        });

        // Open Delete Confirmation Modal
        document.querySelectorAll('.delete-button').forEach(button => {
            button.addEventListener('click', (e) => {
                const accountNumber = e.target.getAttribute('data-account-number');
                deleteAccountNumberSpan.textContent = accountNumber;
                deleteFormAccountNumberInput.value = accountNumber;
                openModal(deleteModal);
            });
        });

        // Close Modals
        closeModalBtn.addEventListener('click', () => {
            closeModal(customerModal);
        });
        cancelDeleteBtn.addEventListener('click', () => {
            closeModal(deleteModal);
        });

        // Close modals if user clicks outside
        window.addEventListener('click', (e) => {
            if (e.target === customerModal) {
                closeModal(customerModal);
            }
            if (e.target === deleteModal) {
                closeModal(deleteModal);
            }
        });
    });
</script>
</body>
</html>
