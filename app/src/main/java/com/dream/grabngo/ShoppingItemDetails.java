package com.dream.grabngo;

public class ShoppingItemDetails {
    private String itemName;
    private int availableQuantity;
    private Double itemPrice;
    private Boolean isAddedToCart;

    public ShoppingItemDetails(String itemName, int availableQuantity, Double itemPrice, Boolean isAddedToCart) {
        this.itemName = itemName;
        this.availableQuantity = availableQuantity;
        this.itemPrice = itemPrice;
        this.isAddedToCart = isAddedToCart;
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

    public Boolean getAddedToCart() {
        return isAddedToCart;
    }

    public void setAddedToCart(Boolean addedToCart) {
        isAddedToCart = addedToCart;
    }
}
