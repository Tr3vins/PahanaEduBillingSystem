package com.pahanaedu.servlet;

import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.dao.ItemDAO;
import com.pahanaedu.dao.BillDAO;
import com.pahanaedu.model.Customer;
import com.pahanaedu.model.Item;
import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//Servlet for managing bill operations.
@WebServlet("/bill")
public class BillServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CustomerDAO customerDAO;
    private ItemDAO itemDAO;
    private BillDAO billDAO;

    public void init() {
        customerDAO = new CustomerDAO();
        itemDAO = new ItemDAO();
        billDAO = new BillDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "create";
        }

        switch (action) {
            case "viewInvoice":
                viewInvoice(request, response);
                break;
            case "create":
            default:
                showCreateBillPage(request, response);
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("generate".equals(action)) {
            generateBill(request, response);
        } else {
            showCreateBillPage(request, response);
        }
    }

    private void showCreateBillPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Customer> listCustomers = customerDAO.getAllCustomers();
        List<Item> listItems = itemDAO.getAllItems();
        request.setAttribute("listCustomers", listCustomers);
        request.setAttribute("listItems", listItems);
        request.getRequestDispatcher("createBill.jsp").forward(request, response);
    }

    private void generateBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountNumber = request.getParameter("customerAccountNumber");
        String[] itemIds = request.getParameterValues("itemId[]");
        String[] quantities = request.getParameterValues("quantity[]");

        if (accountNumber == null || accountNumber.isEmpty() || itemIds == null || quantities == null) {
            request.setAttribute("errorMessage", "Invalid bill data. Please select a customer and at least one item.");
            showCreateBillPage(request, response);
            return;
        }

        try {
            List<BillItem> billItems = new ArrayList<>();
            double totalAmount = 0.0;

            // Retrieve all items from the database once to avoid multiple calls
            List<Item> allItems = itemDAO.getAllItems();

            for (int i = 0; i < itemIds.length; i++) {
                int itemId = Integer.parseInt(itemIds[i]);
                int quantity = Integer.parseInt(quantities[i]);

                // Find the item details from the list
                Item item = allItems.stream().filter(it -> it.getItemId() == itemId).findFirst().orElse(null);

                if (item != null) {
                    if (quantity > item.getStockQuantity()) {
                        request.setAttribute("errorMessage", "Insufficient stock for item: " + item.getItemName());
                        showCreateBillPage(request, response);
                        return;
                    }

                    double subTotal = item.getUnitPrice() * quantity;
                    totalAmount += subTotal;

                    BillItem billItem = new BillItem();
                    billItem.setItemId(itemId);
                    billItem.setItemName(item.getItemName());
                    billItem.setUnitPrice(item.getUnitPrice());
                    billItem.setQuantity(quantity);
                    billItem.setSubTotal(subTotal);
                    billItems.add(billItem);
                }
            }

            Bill newBill = new Bill();
            newBill.setAccountNumber(accountNumber);
            newBill.setTotalAmount(totalAmount);
            newBill.setBillItems(billItems);

            int billId = billDAO.addBill(newBill);

            if (billId != -1) {
                // Update item stock quantities
                for (BillItem item : billItems) {
                    itemDAO.updateItemStock(item.getItemId(), item.getQuantity());
                }

                request.setAttribute("successMessage", "Bill generated successfully!");
                response.sendRedirect(request.getContextPath() + "/bill?action=viewInvoice&billId=" + billId);
            } else {
                request.setAttribute("errorMessage", "Error generating bill. Please try again.");
                showCreateBillPage(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid number format for item ID or quantity.");
            showCreateBillPage(request, response);
        }
    }

    private void viewInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String billIdStr = request.getParameter("billId");
        if (billIdStr == null || billIdStr.isEmpty()) {
            request.setAttribute("errorMessage", "Bill ID not provided.");
            showCreateBillPage(request, response);
            return;
        }

        try {
            int billId = Integer.parseInt(billIdStr);
            Bill bill = billDAO.getBillById(billId);

            if (bill != null) {
                Customer customer = customerDAO.getCustomerByAccountNumber(bill.getAccountNumber());
                request.setAttribute("bill", bill);
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("invoice.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Bill not found.");
                showCreateBillPage(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Bill ID format.");
            showCreateBillPage(request, response);
        }
    }
}