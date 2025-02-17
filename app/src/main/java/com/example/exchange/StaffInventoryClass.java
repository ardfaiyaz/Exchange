package com.example.exchange;

public class StaffInventoryClass {
    private String name;
    private double price;
    private int stock;
    private boolean isChecked;

    public StaffInventoryClass(String name, double price, int stock, boolean isChecked) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.isChecked = isChecked;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public boolean isChecked() { return isChecked; }
    public void setChecked(boolean checked) { isChecked = checked; }
}
