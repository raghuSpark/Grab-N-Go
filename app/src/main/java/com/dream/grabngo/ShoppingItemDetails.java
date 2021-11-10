package com.dream.grabngo;

public class ShoppingItemDetails {
    private String itemName;
    private int availableQuantity;
    private Double itemPrice;

    public ShoppingItemDetails(String itemName, int availableQuantity, Double itemPrice) {
        this.itemName = itemName;
        this.availableQuantity = availableQuantity;
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }
}
