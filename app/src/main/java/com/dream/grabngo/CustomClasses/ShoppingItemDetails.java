package com.dream.grabngo.CustomClasses;

public class ShoppingItemDetails {
    private String shopId, shopName, cityName;
    private String itemName;
    private int availableQuantity;
    private int shopRating;
    private Double itemPrice;

    public ShoppingItemDetails(){}

    public ShoppingItemDetails(String shopId, String shopName, String cityName, String itemName, int availableQuantity, int shopRating, Double itemPrice) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.cityName = cityName;
        this.itemName = itemName;
        this.availableQuantity = availableQuantity;
        this.shopRating = shopRating;
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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public int getShopRating() {
        return shopRating;
    }

    public void setShopRating(int shopRating) {
        this.shopRating = shopRating;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }
}
