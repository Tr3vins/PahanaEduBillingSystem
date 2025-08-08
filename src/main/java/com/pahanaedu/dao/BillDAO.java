package com.pahanaedu.dao;

import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;
import com.pahanaedu.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Data Access Object (DAO) for Bill operations.

public class BillDAO {

    // Adds a new bill and its items to the database.
    public int addBill(Bill bill) {
        String SQL_BILL = "INSERT INTO bills (account_number, total_amount) VALUES (?, ?)";
        String SQL_BILL_ITEM = "INSERT INTO bill_items (bill_id, item_id, item_name, unit_price, quantity, sub_total) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmtBill = null;
        PreparedStatement pstmtBillItem = null;
        ResultSet rs = null;
        int billId = -1;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert into bills table
            pstmtBill = conn.prepareStatement(SQL_BILL, Statement.RETURN_GENERATED_KEYS);
            pstmtBill.setString(1, bill.getAccountNumber());
            pstmtBill.setDouble(2, bill.getTotalAmount());
            int rowsAffectedBill = pstmtBill.executeUpdate();

            if (rowsAffectedBill > 0) {
                rs = pstmtBill.getGeneratedKeys();
                if (rs.next()) {
                    billId = rs.getInt(1);

                    // 2. Insert into bill_items table
                    pstmtBillItem = conn.prepareStatement(SQL_BILL_ITEM);
                    for (BillItem item : bill.getBillItems()) {
                        pstmtBillItem.setInt(1, billId);
                        pstmtBillItem.setInt(2, item.getItemId());
                        pstmtBillItem.setString(3, item.getItemName());
                        pstmtBillItem.setDouble(4, item.getUnitPrice());
                        pstmtBillItem.setInt(5, item.getQuantity());
                        pstmtBillItem.setDouble(6, item.getSubTotal());
                        pstmtBillItem.addBatch(); // Add to batch
                    }
                    pstmtBillItem.executeBatch(); // Execute all inserts

                    conn.commit(); // Commit transaction
                }
            } else {
                conn.rollback(); // Rollback if bill insertion fails
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on any exception
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            billId = -1;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Restore default
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            DBConnection.closeResources(conn, pstmtBill, rs);
            DBConnection.closeResources(null, pstmtBillItem, null);
        }
        return billId;
    }

    //Retrieves a bill and its items by billId.
    public Bill getBillById(int billId) {
        String SQL_BILL = "SELECT * FROM bills WHERE bill_id = ?";
        String SQL_BILL_ITEMS = "SELECT * FROM bill_items WHERE bill_id = ?";
        Connection conn = null;
        PreparedStatement pstmtBill = null;
        PreparedStatement pstmtItems = null;
        ResultSet rsBill = null;
        ResultSet rsItems = null;
        Bill bill = null;

        try {
            conn = DBConnection.getConnection();

            // Get bill details
            pstmtBill = conn.prepareStatement(SQL_BILL);
            pstmtBill.setInt(1, billId);
            rsBill = pstmtBill.executeQuery();

            if (rsBill.next()) {
                bill = new Bill();
                bill.setBillId(rsBill.getInt("bill_id"));
                bill.setAccountNumber(rsBill.getString("account_number"));
                bill.setBillDate(rsBill.getTimestamp("bill_date"));
                bill.setTotalAmount(rsBill.getDouble("total_amount"));

                // Get bill items
                pstmtItems = conn.prepareStatement(SQL_BILL_ITEMS);
                pstmtItems.setInt(1, billId);
                rsItems = pstmtItems.executeQuery();
                List<BillItem> items = new ArrayList<>();
                while (rsItems.next()) {
                    BillItem item = new BillItem();
                    item.setBillItemId(rsItems.getInt("bill_item_id"));
                    item.setBillId(rsItems.getInt("bill_id"));
                    item.setItemId(rsItems.getInt("item_id"));
                    item.setItemName(rsItems.getString("item_name"));
                    item.setUnitPrice(rsItems.getDouble("unit_price"));
                    item.setQuantity(rsItems.getInt("quantity"));
                    item.setSubTotal(rsItems.getDouble("sub_total"));
                    items.add(item);
                }
                bill.setBillItems(items);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(null, pstmtItems, rsItems);
            DBConnection.closeResources(conn, pstmtBill, rsBill);
        }
        return bill;
    }
}