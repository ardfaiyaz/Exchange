package com.example.exchange;

import android.graphics.Bitmap;

public class Product {
    private int productId;
    private String name;
    private double price;
    private Bitmap image;

    public Product(int productId, String name, double price, Bitmap image) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Bitmap getImage() {
        return image;
    }
}
