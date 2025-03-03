package com.example.exchange;

public class TrackOrderItemModel {
    private int productId;
    private String productName;
    private String variantName;
    private int quantity;
    private double price;

    public TrackOrderItemModel(int productId, String productName, String variantName, int quantity, double price) {
        this.productId = productId;
        this.productName = productName;
        this.variantName = variantName;
        this.quantity = quantity;
        this.price = price;
    }

    public int getProductId() {
        return productId;
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
