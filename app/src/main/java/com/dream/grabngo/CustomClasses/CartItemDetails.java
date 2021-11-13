package com.dream.grabngo.CustomClasses;

public class CartItemDetails {
    private String shopId;
    private String itemName;
    private Double itemPrice;
    private int availableQuantity, orderQuantity;

    public CartItemDetails(String shopId, String itemName, Double itemPrice, int availableQuantity, int orderQuantity) {
        this.shopId = shopId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.availableQuantity = availableQuantity;
        this.orderQuantity = orderQuantity;
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

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
}
