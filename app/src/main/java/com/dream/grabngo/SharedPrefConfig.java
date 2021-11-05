package com.dream.grabngo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
}
