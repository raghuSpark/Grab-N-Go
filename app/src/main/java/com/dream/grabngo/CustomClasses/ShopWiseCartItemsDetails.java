package com.dream.grabngo.CustomClasses;

import com.dream.grabngo.CustomClasses.CartItemDetails;

import java.util.ArrayList;

public class ShopWiseCartItemsDetails {
    private String shopId, shopName;
    private ArrayList<CartItemDetails> cartItemDetailsArrayList;

    public ShopWiseCartItemsDetails(String shopId, String shopName, ArrayList<CartItemDetails> cartItemDetailsArrayList) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.cartItemDetailsArrayList = cartItemDetailsArrayList;
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

    public ArrayList<CartItemDetails> getCartItemDetailsArrayList() {
        return cartItemDetailsArrayList;
    }

    public void setCartItemDetailsArrayList(ArrayList<CartItemDetails> cartItemDetailsArrayList) {
        this.cartItemDetailsArrayList = cartItemDetailsArrayList;
    }
}
