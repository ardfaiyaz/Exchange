package com.example.exchange;

public class TrackOrderItemModel {
    private int orderId;
    private String productName;
    private String variantName;
    private int quantity;
    private double price;

    public TrackOrderItemModel(int orderId, String productName, String variantName, int quantity, double price) {
        this.orderId = orderId;
        this.productName = productName;
        this.variantName = variantName;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getProductName() {
        return productName;
    }

    public String getVariantName() {
        return variantName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
