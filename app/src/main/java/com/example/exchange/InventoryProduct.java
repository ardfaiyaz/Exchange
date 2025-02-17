
package com.example.exchange;

import java.math.BigDecimal;

public class InventoryProduct {
    private String productName;
    private BigDecimal productPrice;
    private int productStock;
    private String imageBase64;
    private String varId;

    public InventoryProduct(String productName, BigDecimal productPrice, int productStock, String imageBase64, String varId) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.imageBase64 = imageBase64;
        this.varId = varId;  // Added

    }

    public String getProductName() { return productName; }
    public BigDecimal getProductPrice() { return productPrice; }
    public int getProductStock() { return productStock; }
    public String getImageBase64() { return imageBase64; }
    public String getVarId() { return varId; }//
}