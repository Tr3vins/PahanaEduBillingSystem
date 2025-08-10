<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Items</title>
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
        <h2>Manage Items</h2>
    </div>
    <p class="sub-text">Select an option below to manage your Items:</p>

    <!-- Display Messages -->
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

    <!-- Add Item Button -->
    <div class="flex justify-end mb-6">
        <button id="addItemBtn" class="add-button">
            <i class="fa-solid fa-plus mr-2"></i>Add New Item
        </button>
    </div>

    <div class="table-container">
        <table class="data-table">
            <thead>
            <tr>
                <th>Item ID</th>
                <th>Item Name</th>
                <th>Unit Price</th>
                <th>Stock Quantity</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${listItems}">
                <tr>
                    <td><c:out value="${item.itemId}"/></td>
                    <td><c:out value="${item.itemName}"/></td>
                    <td>Rs. <c:out value="${item.unitPrice}"/></td>
                    <td><c:out value="${item.stockQuantity}"/></td>
                    <td>
                        <button class="edit-button"
                                data-item-id="${item.itemId}"
                                data-item-name="${item.itemName}"
                                data-unit-price="${item.unitPrice}"
                                data-stock-quantity="${item.stockQuantity}">Edit</button>
                        <button class="delete-button"
                                data-item-id="${item.itemId}"
                                data-item-name="${item.itemName}">Delete</button>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty listItems}">
                <tr>
                    <td colspan="5" class="data-table-empty-text">No items found.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>

    <!-- Add/Edit Item Modal -->
    <div id="itemModal" class="modal-overlay">
        <div class="modal-content">
            <button class="modal-close-button" id="closeModalBtn">&times;</button>
            <h2 id="modalTitle" class="modal-title"></h2>
            <form action="item" method="post" id="itemForm" class="space-y-4">
                <input type="hidden" name="action" id="formAction" value="">
                <input type="hidden" name="itemId" id="itemIdInput" value="">

                <div class="form-group">
                    <label for="itemName" class="form-label">Item Name:</label>
                    <input type="text" id="itemNameInput" name="itemName" class="form-input" required>
                </div>

                <div class="form-group">
                    <label for="unitPrice" class="form-label">Unit Price:</label>
                    <input type="number" step="0.01" id="unitPriceInput" name="unitPrice" class="form-input" required>
                </div>

                <div class="form-group">
                    <label for="stockQuantity" class="form-label">Stock Quantity:</label>
                    <input type="number" id="stockQuantityInput" name="stockQuantity" class="form-input" required>
                </div>

                <div class="flex justify-end">
                    <button type="submit" id="submitButton" class="form-button">Add Item</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div id="deleteModal" class="modal-overlay">
        <div class="modal-content">
            <h2 class="modal-title">Confirm Deletion</h2>
            <p class="text-center text-gray-700">Are you sure you want to delete the item: <span id="deleteItemIdSpan" class="font-bold"></span>?</p>
            <form action="item" method="post" class="modal-container">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="itemId" id="deleteFormItemIdInput" value="">
                <button type="button" id="cancelDeleteBtn" class="modal-cancel-button">Cancel</button>
                <button type="submit" class="modal-delete-button">Delete</button>
            </form>
        </div>
    </div>

</div>
<script>
    const itemModal = document.getElementById('itemModal');
    const deleteModal = document.getElementById('deleteModal');
    const addItemBtn = document.getElementById('addItemBtn');
    const closeModalBtn = document.getElementById('closeModalBtn');
    const cancelDeleteBtn = document.getElementById('cancelDeleteBtn');

    // Form elements for the add/edit modal
    const modalTitle = document.getElementById('modalTitle');
    const itemForm = document.getElementById('itemForm');
    const formAction = document.getElementById('formAction');
    const submitButton = document.getElementById('submitButton');
    const itemIdInput = document.getElementById('itemIdInput');
    const itemNameInput = document.getElementById('itemNameInput');
    const unitPriceInput = document.getElementById('unitPriceInput');
    const stockQuantityInput = document.getElementById('stockQuantityInput');

    // Elements for the delete modal
    const deleteItemIdSpan = document.getElementById('deleteItemIdSpan');
    const deleteFormItemIdInput = document.getElementById('deleteFormItemIdInput');

    // Functions to open and close modals
    function openModal(modal) {
        modal.classList.add('show');
        document.body.style.overflow = 'hidden'; // Prevent scrolling
    }

    function closeModal(modal) {
        modal.classList.remove('show');
        document.body.style.overflow = 'auto'; // Re-enable scrolling
    }

    // Event listener to open the "Add New Item" modal
    addItemBtn.addEventListener('click', () => {
        modalTitle.textContent = 'Add New Item';
        formAction.value = 'add';
        submitButton.textContent = 'Add Item';
        itemForm.reset(); // Clear form fields
        itemIdInput.readOnly = false;
        itemIdInput.classList.remove('bg-gray-200');
        itemIdInput.value = ''; // Ensure itemId is empty for new items
        openModal(itemModal);
    });

    // Event listeners to open the "Edit" modal
    document.querySelectorAll('.edit-button').forEach(button => {
        button.addEventListener('click', (e) => {
            const target = e.currentTarget;
            const itemId = target.getAttribute('data-item-id');
            const itemName = target.getAttribute('data-item-name');
            const unitPrice = target.getAttribute('data-unit-price');
            const stockQuantity = target.getAttribute('data-stock-quantity');

            modalTitle.textContent = 'Edit Item';
            formAction.value = 'update';
            submitButton.textContent = 'Update Item';

            itemIdInput.value = itemId;
            itemNameInput.value = itemName;
            unitPriceInput.value = unitPrice;
            stockQuantityInput.value = stockQuantity;

            // Set itemId to read-only for editing
            itemIdInput.readOnly = true;
            itemIdInput.classList.add('bg-gray-200');
            openModal(itemModal);
        });
    });

    // Event listeners to open the "Delete" confirmation modal
    document.querySelectorAll('.delete-button').forEach(button => {
        button.addEventListener('click', (e) => {
            const target = e.currentTarget;
            const itemId = target.getAttribute('data-item-id');
            deleteItemIdSpan.textContent = itemId;
            deleteFormItemIdInput.value = itemId;
            openModal(deleteModal);
        });
    });

    // Close Modals
    closeModalBtn.addEventListener('click', () => {
        closeModal(itemModal);
    });
    cancelDeleteBtn.addEventListener('click', () => {
        closeModal(deleteModal);
    });

    // Close modals if user clicks outside
    window.addEventListener('click', (e) => {
        if (e.target === itemModal) {
            closeModal(itemModal);
        }
        if (e.target === deleteModal) {
            closeModal(deleteModal);
        }
    });
</script>
</body>
</html>