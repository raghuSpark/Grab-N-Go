package com.dream.grabngo.CustomClasses;

public class ShoppingItemDetails {
    private String shopId;
    private String itemName;
    private int availableQuantity;
    private Double itemPrice;

    public ShoppingItemDetails(String shopId, String itemName, int availableQuantity, Double itemPrice) {
        this.shopId = shopId;
        this.itemName = itemName;
        this.availableQuantity = availableQuantity;
        this.itemPrice = itemPrice;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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
