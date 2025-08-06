package com.pahanaedu.model;

// Model class representing an Item in the system.

public class Item {
    private int itemId;
    private String itemName;
    private double unitPrice;
    private int stockQuantity;

    // Constructors
    public Item() {
    }

    public Item(int itemId, String itemName, double unitPrice, int stockQuantity) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
    }

    public Item(String itemName, double unitPrice, int stockQuantity) {
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
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

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", unitPrice=" + unitPrice +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}