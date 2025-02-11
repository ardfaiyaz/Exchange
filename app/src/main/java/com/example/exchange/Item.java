package com.example.exchange;

public class Item {
    private final String name;
    private final String description;
    private final String price;

    public Item(String name, String description, String price) {
        this.name = name;
        this.description = description;
        this.price = price;
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
}
