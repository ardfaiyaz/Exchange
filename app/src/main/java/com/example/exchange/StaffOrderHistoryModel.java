package com.example.exchange;

public class StaffOrderHistoryModel {
    private String orderId, userId, customerUsername, productName, productImage, productVar, orderStatus, dateCompleted;
    private int quantity;
    private double totalPrice;

    public StaffOrderHistoryModel(String orderId, String userId, String customerUsername, String productName, String productImage,
                                  String productVar, String orderStatus, String dateCompleted, int quantity, double totalPrice) {
        this.orderId = orderId;
        this.userId = userId;  // ✅ Include user_id
        this.customerUsername = customerUsername;
        this.productName = productName;
        this.productImage = productImage;
        this.productVar = productVar;
        this.orderStatus = orderStatus;
        this.dateCompleted = dateCompleted;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }  // ✅ Getter for user_id
    public String getCustomerUsername() { return customerUsername; }
    public String getProductName() { return productName; }
    public String getProductImage() { return productImage; }
    public String getProductVar() { return productVar; }
    public String getOrderStatus() { return orderStatus; }
    public String getDateCompleted() { return dateCompleted; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return totalPrice; }
}
