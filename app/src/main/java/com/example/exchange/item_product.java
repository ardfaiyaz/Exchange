package com.example.exchange;

public class item_product {
    private String name;
    private String price;
    private String stock;
    private int imageResource;

    public item_product(String name, String price, String stock, int imageResource) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getStock() {
        return stock;
    }

    public int getImageResource() {
        return imageResource;
    }
}
v