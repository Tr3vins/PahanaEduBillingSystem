package com.pahanaedu.servlet;

import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.model.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

// Servlet for managing customer operations (CRUD).

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CustomerDAO customerDAO;

    public void init() {
        customerDAO = new CustomerDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "view"; // Default action
        }

        // The doPost method handles all the form submissions from the modals.
        switch (action) {
            case "add":
                addCustomer(request, response);
                break;
            case "update":
                updateCustomer(request, response);
                break;
            case "delete":
                deleteCustomer(request, response);
                break;
            default:
                manageCustomers(request, response);
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // The doGet method only handles viewing the customer list.
        String action = request.getParameter("action");
        if (action == null || action.equals("view")) {
            manageCustomers(request, response);
        } else {
            // Redirect to view all customers if an invalid GET action is requested
            response.sendRedirect("customer?action=view");
        }
    }

    private void addCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountNumber = request.getParameter("accountNumber");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String telephoneNumber = request.getParameter("telephoneNumber");

        Customer newCustomer = new Customer(accountNumber, name, address, telephoneNumber);

        if (customerDAO.accountNumberExists(accountNumber)) {
            request.setAttribute("errorMessage", "Error: A customer with this account number already exists.");
        } else {
            if (customerDAO.addCustomer(newCustomer)) {
                request.setAttribute("successMessage", "Customer added successfully!");
            } else {
                request.setAttribute("errorMessage", "Error adding customer. Please try again.");
            }
        }
        manageCustomers(request, response);
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountNumber = request.getParameter("accountNumber");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String telephoneNumber = request.getParameter("telephoneNumber");

        Customer customerToUpdate = new Customer(accountNumber, name, address, telephoneNumber);

        if (customerDAO.updateCustomer(customerToUpdate)) {
            request.setAttribute("successMessage", "Customer updated successfully!");
        } else {
            request.setAttribute("errorMessage", "Error updating customer. Please try again.");
        }
        manageCustomers(request, response);
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountNumber = request.getParameter("accountNumber");
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Account Number is required for deletion.");
        } else {
            if (customerDAO.deleteCustomer(accountNumber)) {
                request.setAttribute("successMessage", "Customer deleted successfully!");
            } else {
                request.setAttribute("errorMessage", "Error deleting customer. Ensure no related bills exist.");
            }
        }
        manageCustomers(request, response);
    }

    private void manageCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Customer> listCustomers = customerDAO.getAllCustomers();
        request.setAttribute("listCustomers", listCustomers);
        request.getRequestDispatcher("manageCustomers.jsp").forward(request, response);
    }
}