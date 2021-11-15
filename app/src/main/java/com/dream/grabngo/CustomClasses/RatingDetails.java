package com.dream.grabngo.CustomClasses;

public class RatingDetails {
    private String shopId, shopName;
    private String customerId, customerName;
    private String customerReview;
    private int ratingValue;
    private String ratingTimeStamp;

    public RatingDetails(String shopId, String shopName, String customerId, String customerName, String customerReview, int ratingValue, String ratingTimeStamp) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerReview = customerReview;
        this.ratingValue = ratingValue;
        this.ratingTimeStamp = ratingTimeStamp;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerReview() {
        return customerReview;
    }

    public void setCustomerReview(String customerReview) {
        this.customerReview = customerReview;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getRatingTimeStamp() {
        return ratingTimeStamp;
    }

    public void setRatingTimeStamp(String ratingTimeStamp) {
        this.ratingTimeStamp = ratingTimeStamp;
    }
}
