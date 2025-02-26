package com.example.exchange;

import android.graphics.Bitmap;

public class Item {
    private int cartId;
    private int productId;
    private String name;
    private String variation;
    private String price;
    private Bitmap image;
    private int quantity;
    private boolean isSelected;

    public Item(int cartId, int productId, String name, String variation, String price, Bitmap image, int quantity) {
        this.cartId = cartId;
        this.productId = productId;
        this.name = name;
        this.variation = variation;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
        this.isSelected = false;
    }

    public int getCartId() {
        return cartId;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
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
