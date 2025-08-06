package com.pahanaedu.servlet;

import com.pahanaedu.dao.ItemDAO;
import com.pahanaedu.model.Item;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

//Servlet for managing item operations (CRUD).

@WebServlet("/item")
public class ItemServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ItemDAO itemDAO;

    public void init() {
        itemDAO = new ItemDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "view"; // Default action
        }

        switch (action) {
            case "add":
                addItem(request, response);
                break;
            case "update":
                updateItem(request, response);
                break;
            case "delete":
                deleteItem(request, response);
                break;
            default:
                viewItems(request, response);
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // doGet is only for viewing the list of items
        viewItems(request, response);
    }

    private void addItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String itemName = request.getParameter("itemName");
        String unitPriceStr = request.getParameter("unitPrice");
        String stockQuantityStr = request.getParameter("stockQuantity");

        try {
            double unitPrice = Double.parseDouble(unitPriceStr);
            int stockQuantity = Integer.parseInt(stockQuantityStr);

            if (itemDAO.itemNameExists(itemName)) {
                request.setAttribute("errorMessage", "An item with this name already exists. Please choose a different name.");
            } else {
                Item newItem = new Item(itemName, unitPrice, stockQuantity);
                if (itemDAO.addItem(newItem)) {
                    request.setAttribute("successMessage", "Item added successfully!");
                } else {
                    request.setAttribute("errorMessage", "Error adding item. Please try again.");
                }
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid number format for Unit Price or Stock Quantity.");
        }
        viewItems(request, response);
    }

    private void updateItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String itemIdStr = request.getParameter("itemId");
        String itemName = request.getParameter("itemName");
        String unitPriceStr = request.getParameter("unitPrice");
        String stockQuantityStr = request.getParameter("stockQuantity");

        try {
            int itemId = Integer.parseInt(itemIdStr);
            double unitPrice = Double.parseDouble(unitPriceStr);
            int stockQuantity = Integer.parseInt(stockQuantityStr);

            Item itemToUpdate = new Item(itemId, itemName, unitPrice, stockQuantity);
            if (itemDAO.updateItem(itemToUpdate)) {
                request.setAttribute("successMessage", "Item updated successfully!");
            } else {
                request.setAttribute("errorMessage", "Error updating item. Please try again.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid number format for Unit Price or Stock Quantity.");
        }
        viewItems(request, response);
    }

    private void deleteItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String itemIdStr = request.getParameter("itemId");

        try {
            int itemId = Integer.parseInt(itemIdStr);
            if (itemDAO.deleteItem(itemId)) {
                request.setAttribute("successMessage", "Item deleted successfully!");
            } else {
                request.setAttribute("errorMessage", "Error deleting item. Please try again.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Item ID format.");
        }
        viewItems(request, response);
    }

    private void viewItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Item> listItems = itemDAO.getAllItems();
        request.setAttribute("listItems", listItems);
        request.getRequestDispatcher("manageItems.jsp").forward(request, response);
    }
}
