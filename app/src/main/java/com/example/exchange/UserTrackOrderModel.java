package com.example.exchange;

import android.graphics.Bitmap;

public class UserTrackOrderModel {
    private int orderId;
    private String productName;
    private String productSize;
    private int quantity;
    private double productPrice;
    private Bitmap productImage; // Drawable resource ID

    public UserTrackOrderModel(int orderId, String productName, String productSize, int quantity, double productPrice, Bitmap productImage) {
        this.orderId = orderId;
        this.productName = productName;
        this.productSize = productSize;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.productImage = productImage;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductSize() {
        return productSize;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public Bitmap getProductImage() {
        return productImage;
    }
}