<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Create Bill</title>
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
    <h2>Create New Bill</h2>
  </div>
  <p class="sub-text">Select an option below to Create a Bill:</p>

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

  <form id="billForm" action="bill" method="post" class="form-container">
    <input type="hidden" name="action" value="generate">

    <div class="form-group">
      <label for="customer" class="form-label">Select Customer</label>
      <select id="customer" name="customerAccountNumber" class="form-input" required>
        <option value="">-- Select a Customer --</option>
        <c:forEach var="customer" items="${listCustomers}">
          <option value="${customer.accountNumber}">${customer.accountNumber} - ${customer.name}</option>
        </c:forEach>
      </select>
    </div>

    <div class="form-section">
      <div class="form-item-row">
        <div class="form-group flex-1">
          <label for="itemSelector" class="form-label">Select Item</label>
          <select id="itemSelector" class="form-input">
            <option value="">-- Select an Item --</option>
            <c:forEach var="item" items="${listItems}">
              <option value="${item.itemId}" data-price="${item.unitPrice}" data-stock="${item.stockQuantity}">${item.itemName} (Stock: ${item.stockQuantity})</option>
            </c:forEach>
          </select>
        </div>
        <div class="form-group item-quantity-container">
          <label for="itemQuantity" class="form-label">Quantity</label>
          <input type="number" id="itemQuantity" value="1" min="1" class="form-input">
        </div>
        <div class="form-group">
          <button type="button" id="addItemButton" class="add-button">Add Item</button>
        </div>
      </div>

      <div class="table-container">
        <table class="data-table">
          <thead>
          <tr>
            <th>Item Name</th>
            <th>Unit Price</th>
            <th>Quantity</th>
            <th>Sub Total</th>
            <th>Actions</th>
          </tr>
          </thead>
          <tbody id="billItemsTableBody">
          </tbody>
        </table>
      </div>
      <div class="total-display-wrapper">
        Total: <span id="totalAmountDisplay">Rs. 0.00</span>
      </div>
    </div>

    <div class="flex justify-end mt-6">
      <button type="submit" id="submitBillButton" class="form-button">Generate Bill</button>
    </div>
  </form>
</div>

<!-- Alert Modal -->
<div id="alertModal" class="modal-overlay">
  <div class="modal-content">
    <h2 class="modal-title text-red-500">Error</h2>
    <p id="alertMessage" class="text-center text-gray-100 my-4"></p>
    <div class="flex justify-center">
      <button type="button" id="closeAlertBtn" class="modal-cancel-button">OK</button>
    </div>
  </div>
</div>


<script>
  document.addEventListener('DOMContentLoaded', () => {
    const addItemButton = document.getElementById('addItemButton');
    const itemSelector = document.getElementById('itemSelector');
    const itemQuantityInput = document.getElementById('itemQuantity');
    const billItemsTableBody = document.getElementById('billItemsTableBody');
    const totalAmountDisplay = document.getElementById('totalAmountDisplay');
    const billForm = document.getElementById('billForm');
    let totalAmount = 0.0;

    // Track the stock for each item
    const stockData = {};

    // Modal-related elements
    const alertModal = document.getElementById('alertModal');
    const alertMessage = document.getElementById('alertMessage');
    const closeAlertBtn = document.getElementById('closeAlertBtn');

    // Function to open a modal
    function openModal(modal) {
      modal.classList.add('show');
      document.body.style.overflow = 'hidden'; // Prevent scrolling
    }

    // Function to close a modal
    function closeModal(modal) {
      modal.classList.remove('show');
      document.body.style.overflow = 'auto'; // Re-enable scrolling
    }

    // New function to show the alert modal with a specific message
    function showAlertModal(message) {
      alertMessage.textContent = message;
      openModal(alertModal);
    }

    // Function to update the total amount displayed
    function updateTotalAmount() {
      totalAmountDisplay.textContent = 'Rs. ' + totalAmount.toFixed(2);
    }

    // Update the dropdown options based on stock
    function updateItemSelector() {
      const options = itemSelector.querySelectorAll('option');
      options.forEach(option => {
        const itemId = option.value;
        const stock = stockData[itemId] || 0;
        if (stock <= 0) {
          option.disabled = true;
        } else {
          option.disabled = false;
        }
      });
    }

    // Add a new item row to the table
    addItemButton.addEventListener('click', () => {
      const selectedOption = itemSelector.options[itemSelector.selectedIndex];
      const itemId = selectedOption.value;
      const itemName = selectedOption.textContent.split('(')[0].trim();
      const unitPrice = parseFloat(selectedOption.getAttribute('data-price'));
      const stockQuantity = parseInt(selectedOption.getAttribute('data-stock'));
      const quantityToAdd = parseInt(itemQuantityInput.value);

      if (!itemId || quantityToAdd <= 0) {
        showAlertModal('Please select a valid item.');
        return;
      }

      // Check if item is already in the table by iterating through rows.
      let existingRow = null;
      const allRows = billItemsTableBody.querySelectorAll('tr');
      for (const row of allRows) {
        if (row.getAttribute('data-item-id') === itemId) {
          existingRow = row;
          break;
        }
      }

      if (existingRow) {
        const existingQuantityCell = existingRow.querySelector('.item-quantity span');
        const existingQuantity = parseInt(existingQuantityCell.textContent);
        const newQuantity = existingQuantity + quantityToAdd;

        if (newQuantity > stockQuantity) {
          showAlertModal('Cannot add more than the available stock.');
          return;
        }

        existingQuantityCell.textContent = newQuantity;
        const newSubTotal = newQuantity * unitPrice;
        existingRow.querySelector('.item-subtotal').textContent = 'Rs. ' + newSubTotal.toFixed(2);

        totalAmount += quantityToAdd * unitPrice;
        // Update the hidden input for quantity
        existingRow.querySelector('input[name="quantity[]"]').value = newQuantity;
      } else {
        if (quantityToAdd > stockQuantity) {
          showAlertModal('Cannot add more than the available stock.');
          return;
        }

        const subTotal = unitPrice * quantityToAdd;
        totalAmount += subTotal;
        // Create a new table row element
        const newRow = document.createElement('tr');
        newRow.className = 'border-b border-gray-200 hover:bg-gray-100';
        newRow.setAttribute('data-item-id', itemId);

        // Create and append cells one by one for more robust DOM manipulation
        const itemNameCell = document.createElement('td');
        itemNameCell.className = 'table-cell-padding text-left whitespace-nowrap';

        const itemNameSpan = document.createElement('span');
        itemNameSpan.className = 'font-medium';
        itemNameSpan.textContent = itemName;

        const itemIdInput = document.createElement('input');
        itemIdInput.type = 'hidden';
        itemIdInput.name = 'itemId[]';
        itemIdInput.value = itemId;

        itemNameCell.appendChild(itemNameSpan);
        itemNameCell.appendChild(itemIdInput);
        newRow.appendChild(itemNameCell);

        const unitPriceCell = document.createElement('td');
        unitPriceCell.className = 'table-cell-padding text-left';
        unitPriceCell.textContent = 'Rs. ' + unitPrice.toFixed(2);
        newRow.appendChild(unitPriceCell);

        const quantityCell = document.createElement('td');
        quantityCell.className = 'table-cell-padding text-left item-quantity';
        const quantitySpan = document.createElement('span');
        quantitySpan.textContent = quantityToAdd;

        const quantityInput = document.createElement('input');
        quantityInput.type = 'hidden';
        quantityInput.name = 'quantity[]';
        quantityInput.value = quantityToAdd;
        quantityCell.appendChild(quantitySpan);
        quantityCell.appendChild(quantityInput);
        newRow.appendChild(quantityCell);

        const subTotalCell = document.createElement('td');
        subTotalCell.className = 'table-cell-padding text-left item-subtotal';
        subTotalCell.textContent = 'Rs. ' + subTotal.toFixed(2);
        newRow.appendChild(subTotalCell);

        const actionsCell = document.createElement('td');
        actionsCell.className = 'table-cell-padding text-center';
        actionsCell.innerHTML = `
          <div class="flex item-center justify-center">
            <button type="button" class="remove-item-button">
              <i class="fas fa-trash-alt"></i>
            </button>
          </div>
        `;
        newRow.appendChild(actionsCell);

        billItemsTableBody.appendChild(newRow);

        // Update stock data after adding an item
        stockData[itemId] = stockQuantity - quantityToAdd;
      }

      updateItemSelector();

      updateTotalAmount();
      itemSelector.value = ""; // Reset item selector after adding
      itemQuantityInput.value = 1; // Reset quantity to 1
    });

    // Use event delegation for the remove button
    billItemsTableBody.addEventListener('click', (e) => {
      if (e.target.closest('.remove-item-button')) {
        const row = e.target.closest('tr');
        // Remove 'Rs. ' before parsing to ensure correct calculation
        const subTotalToRemove = parseFloat(row.querySelector('.item-subtotal').textContent.replace('Rs. ', ''));
        totalAmount -= subTotalToRemove;

        const itemId = row.getAttribute('data-item-id');
        const quantityToRemove = parseInt(row.querySelector('.item-quantity span').textContent);

        // Replenish stock after removing an item
        stockData[itemId] += quantityToRemove;

        updateItemSelector();

        updateTotalAmount();
        row.remove();
      }
    });

    // Close Modals
    closeAlertBtn.addEventListener('click', () => {
      closeModal(alertModal);
    });

    // Close modals if user clicks outside
    window.addEventListener('click', (e) => {
      if (e.target === alertModal) {
        closeModal(alertModal);
      }
    });

    // Initial stock data setup for items
    const itemOptions = itemSelector.querySelectorAll('option');
    itemOptions.forEach(option => {
      const itemId = option.value;
      const stock = parseInt(option.getAttribute('data-stock'));
      stockData[itemId] = stock;
    });
    updateItemSelector();
  });
</script>
</body>
</html>