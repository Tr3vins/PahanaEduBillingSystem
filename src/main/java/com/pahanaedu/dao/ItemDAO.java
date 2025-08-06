package com.pahanaedu.dao;

import com.pahanaedu.model.Item;
import com.pahanaedu.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//DAO for Item operations.

public class ItemDAO {

    //Adds a new item to the database.
    public boolean addItem(Item item) {
        String SQL = "INSERT INTO items (item_name, unit_price, stock_quantity) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, item.getItemName());
            pstmt.setDouble(2, item.getUnitPrice());
            pstmt.setInt(3, item.getStockQuantity());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(conn, pstmt, null);
        }
        return success;
    }

    //Retrieves an item by its ID.
    public Item getItemById(int itemId) {
        String SQL = "SELECT * FROM items WHERE item_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Item item = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, itemId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                item = new Item();
                item.setItemId(rs.getInt("item_id"));
                item.setItemName(rs.getString("item_name"));
                item.setUnitPrice(rs.getDouble("unit_price"));
                item.setStockQuantity(rs.getInt("stock_quantity"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(conn, pstmt, rs);
        }
        return item;
    }

    //Retrieves a list of all items from the database.
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String SQL = "SELECT * FROM items";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                Item item = new Item();
                item.setItemId(rs.getInt("item_id"));
                item.setItemName(rs.getString("item_name"));
                item.setUnitPrice(rs.getDouble("unit_price"));
                item.setStockQuantity(rs.getInt("stock_quantity"));
                items.add(item);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(conn, stmt, rs);
        }
        return items;
    }

    // Updates an existing item in the database.
    public boolean updateItem(Item item) {
        String SQL = "UPDATE items SET item_name = ?, unit_price = ?, stock_quantity = ? WHERE item_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, item.getItemName());
            pstmt.setDouble(2, item.getUnitPrice());
            pstmt.setInt(3, item.getStockQuantity());
            pstmt.setInt(4, item.getItemId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(conn, pstmt, null);
        }
        return success;
    }

    //Deletes an item from the database by its ID.
    public boolean deleteItem(int itemId) {
        String SQL = "DELETE FROM items WHERE item_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, itemId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(conn, pstmt, null);
        }
        return success;
    }

    // Checks if an item name already exists (case-insensitive).
    public boolean itemNameExists(String itemName) {
        String SQL = "SELECT COUNT(*) FROM items WHERE LOWER(item_name) = LOWER(?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, itemName);
            rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                exists = true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(conn, pstmt, rs);
        }
        return exists;
    }
}