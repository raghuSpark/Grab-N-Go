package com.dream.grabngo.CustomClasses;

public class ShoppingItemDetails {
    private String shopId, shopName;
    private String itemName;
    private int availableQuantity;
    private Double itemPrice;

    public ShoppingItemDetails(String shopId, String shopName, String itemName, int availableQuantity, Double itemPrice) {
        this.shopId = shopId;
        this.shopName = shopName;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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
