package com.example.exchange;

import android.graphics.Bitmap;

public class Item {
    private String name;
    private String variation;
    private String price;
    private Bitmap image;
    private int quantity;
    private boolean isSelected; // Add this field
    private String productId;

    public Item(String name, String variation, String price, Bitmap image, int quantity) {
        this.name = name;
        this.variation = variation;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
        this.isSelected = false; // Default to false
    }

    public String getName() {
        return name;
    }
    public String getProductId() {
        return productId;
    }

    public String getVariation() {
        return variation;
    }

    public String getPrice() {
        return price;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
