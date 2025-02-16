package com.example.exchange;

import android.graphics.Bitmap;

public class Item {
    private final String name;
    private final String description;
    private final String price;
    private final Bitmap image; // New field for image

    public Item(String name, String description, String price, Bitmap image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public Bitmap getImage() {
        return image;
    }
}
