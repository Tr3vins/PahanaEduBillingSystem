package com.pahanaedu.model;

//Model class representing a billitem on a Bill.

public class BillItem {
    private int billItemId;
    private int billId;
    private int itemId;
    private String itemName;
    private double unitPrice;
    private int quantity;
    private double subTotal;

    // Constructors
    public BillItem() {
    }

    public BillItem(int billItemId, int billId, int itemId, String itemName, double unitPrice, int quantity, double subTotal) {
        this.billItemId = billItemId;
        this.billId = billId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    // Getters and Setters
    public int getBillItemId() {
        return billItemId;
    }

    public void setBillItemId(int billItemId) {
        this.billItemId = billItemId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}