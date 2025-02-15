package com.example.exchange;

import android.graphics.Bitmap;

public class Product {
    private String name;
    private double price;
    private Bitmap image;

    public Product(String name, double price, Bitmap image) {
        this.name = name;
        this.price = price;
        this.image = image;
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
