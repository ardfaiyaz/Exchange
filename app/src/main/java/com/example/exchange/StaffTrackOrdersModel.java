package com.example.exchange;

public class StaffTrackOrdersModel {
    private String userId;
    private String orderId;
    private String productId;
    private String productName;
    private String productImage;
    private String variant;
    private int quantity;
    private double price;
    private String orderStatus;

    public StaffTrackOrdersModel(String userId, String orderId, String productId, String productName, String productImage,
                                 String variant, int quantity, double price, String orderStatus) {
        this.userId = userId;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.variant = variant;
        this.quantity = quantity;
        this.price = price;
        this.orderStatus = orderStatus;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getOrderId() { return orderId; }
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getProductImage() { return productImage; }
    public String getVariant() { return variant; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public String getOrderStatus() { return orderStatus; }

    // Setter for updating status
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
}
