package com.example.exchange;

public class TrackOrderItemModel {
    private String name;
    private String version;
    private String orderId;
    private int quantity;
    private double price;

    // Constructor
    public TrackOrderItemModel(String name, String version, String orderId, int quantity, double price) {
        this.name = name;
        this.version = version;
        this.orderId = orderId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    // Setters (if needed)
    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
