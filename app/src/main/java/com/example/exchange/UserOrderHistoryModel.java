package com.example.exchange;

public class UserOrderHistoryModel {
    private String orderId, orderStatus, productName, productImage, productVar, dateCompleted;
    private int quantity;
    private double totalPrice;

    public UserOrderHistoryModel(String orderId, String orderStatus, String productName, String productImage,
                                 String productVar, String dateCompleted, int quantity, double totalPrice) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.productName = productName;
        this.productImage = productImage;
        this.productVar = productVar;
        this.dateCompleted = dateCompleted;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getOrderId() { return orderId; }
    public String getOrderStatus() { return orderStatus; } // ✅ FIXED method name
    public String getProductName() { return productName; }
    public String getProductImage() { return productImage; } // ✅ FIXED method name
    public String getProductVar() { return productVar; } // ✅ FIXED method name
    public String getDateCompleted() { return dateCompleted; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return totalPrice; }
}
