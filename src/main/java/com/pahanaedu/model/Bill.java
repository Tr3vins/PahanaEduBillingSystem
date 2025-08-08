package com.pahanaedu.model;

import java.sql.Timestamp;
import java.util.List;

//Model class representing a Bill in the system.

public class Bill {
    private int billId;
    private String accountNumber;
    private Timestamp billDate;
    private double totalAmount;
    private List<BillItem> billItems; // List of items on the bill

    // Constructors
    public Bill() {
    }

    public Bill(int billId, String accountNumber, Timestamp billDate, double totalAmount) {
        this.billId = billId;
        this.accountNumber = accountNumber;
        this.billDate = billDate;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Timestamp getBillDate() {
        return billDate;
    }

    public void setBillDate(Timestamp billDate) {
        this.billDate = billDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<BillItem> getBillItems() {
        return billItems;
    }

    public void setBillItems(List<BillItem> billItems) {
        this.billItems = billItems;
    }
}