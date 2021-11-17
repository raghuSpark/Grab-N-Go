package com.dream.grabngo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dream.grabngo.CustomClasses.ShopWiseCartItemsDetails;
import com.dream.grabngo.CustomClasses.ShoppingItemDetails;
import com.dream.grabngo.CustomClasses.UserDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPrefConfig {
    public static void writeIsLoggedIn(Context context, boolean isLoggedIn) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("IS_LOGGED_IN", isLoggedIn);
        editor.apply();
    }

    public static boolean readIsLoggedIn(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        return pref.getBoolean("IS_LOGGED_IN", false);
    }

    public static void writeRefreshTokenRaw(Context context, String raw) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("REFRESH_TOKEN_RAW", raw);
        editor.apply();
    }

    public static String readRefreshTokenRaw(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        return pref.getString("REFRESH_TOKEN_RAW", "");
    }

    public static void writeAreDetailsGiven(Context context, boolean areDetailsGiven) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("ARE_DETAILS_GIVEN", areDetailsGiven);
        editor.apply();
    }

    public static boolean readAreDetailsGiven(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        return pref.getBoolean("ARE_DETAILS_GIVEN", false);
    }

    public static void writeUserDetails(Context context, UserDetails userDetails) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(userDetails);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("USER_DETAILS", jsonString);
        editor.apply();
    }

    public static UserDetails readUserDetails(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString("USER_DETAILS", "");

        Gson gson = new Gson();
        Type type = new TypeToken<UserDetails>() {
        }.getType();
        if (gson.fromJson(jsonString, type) == null) return new UserDetails();
        return gson.fromJson(jsonString, type);
    }

    public static void writeSearchExpandItemDetails(Context context, ShoppingItemDetails shoppingItemDetails) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(shoppingItemDetails);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("SEARCH_EXPAND_ITEM_DETAILS", jsonString);
        editor.apply();
    }

    public static ShoppingItemDetails readSearchExpandItemDetails(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString("SEARCH_EXPAND_ITEM_DETAILS", "");

        Gson gson = new Gson();
        Type type = new TypeToken<ShoppingItemDetails>() {
        }.getType();
        if (gson.fromJson(jsonString, type) == null) return new ShoppingItemDetails();
        return gson.fromJson(jsonString, type);
    }

    public static void writeShopWiseCartItems(Context context, ArrayList<ShopWiseCartItemsDetails> shopWiseCartItemsDetailsArrayList) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(shopWiseCartItemsDetailsArrayList);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("SHOP_WISE_CART_ITEMS", jsonString);
        editor.apply();
    }

    public static ArrayList<ShopWiseCartItemsDetails> readShopWiseCartItems(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString("SHOP_WISE_CART_ITEMS", "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ShopWiseCartItemsDetails>>() {
        }.getType();
        if (gson.fromJson(jsonString, type) == null) return new ArrayList<>();
        return gson.fromJson(jsonString, type);
    }

    public static void writeShopWiseCartItemsHistory(Context context, ArrayList<ShopWiseCartItemsDetails> shopWiseCartItemsDetailsArrayList) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(shopWiseCartItemsDetailsArrayList);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("SHOP_WISE_CART_ITEMS_HISTORY", jsonString);
        editor.apply();
    }

    public static ArrayList<ShopWiseCartItemsDetails> readShopWiseCartItemsHistory(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString("SHOP_WISE_CART_ITEMS_HISTORY", "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ShopWiseCartItemsDetails>>() {
        }.getType();
        if (gson.fromJson(jsonString, type) == null) return new ArrayList<>();
        return gson.fromJson(jsonString, type);
    }
}
