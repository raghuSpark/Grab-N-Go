package com.dream.grabngo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

    public static void writeUserDetails(Context context, JSONObject userDetailsJsonObject) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(userDetailsJsonObject);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("USER_DETAILS", jsonString);
        editor.apply();
    }

    public static JSONObject readUserDetails(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString("USER_DETAILS", "");

        Gson gson = new Gson();
        Type type = new TypeToken<JSONObject>() {
        }.getType();
        if (gson.fromJson(jsonString, type) == null) return new JSONObject();
        return gson.fromJson(jsonString, type);
    }

    public static void writeCartItems(Context context, ArrayList<CartItemDetails> cartItemsArrayList) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(cartItemsArrayList);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("CART_ITEMS", jsonString);
        editor.apply();
    }

    public static ArrayList<CartItemDetails> readCartItems(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString("CART_ITEMS", "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CartItemDetails>>() {
        }.getType();
        if (gson.fromJson(jsonString, type) == null) return new ArrayList<>();
        return gson.fromJson(jsonString, type);
    }
}
