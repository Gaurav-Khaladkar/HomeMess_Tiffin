package com.homemess.tiffinservice.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences Helper for session management
 */
public class PrefsHelper {

    private static final String PREF_NAME = "HomeMessPrefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private final SharedPreferences prefs;

    public PrefsHelper(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserSession(int userId, String name, String role) {
        prefs.edit()
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_USER_NAME, name)
                .putString(KEY_USER_ROLE, role)
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }

    public String getUserRole() {
        return prefs.getString(KEY_USER_ROLE, "");
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
}
