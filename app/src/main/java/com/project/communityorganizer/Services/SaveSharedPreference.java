package com.project.communityorganizer.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by
 * @author Seshagiri on 22/2/15.
 */
public class SaveSharedPreference {
    static final String USER_EMAIL = "email";
    static final String USER_NAME = "username";
    static final String LOGGED_IN = "false";
    static final String LOCATION_SERVICE = "false";
    static final String GEOFENCE = "false";

    /**
     * Returns the SharedPreference object
     * @param context
     * @return
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Changes the USER_EMAIL
     * @param context
     * @param email
     */
    public static void setUserEmail(Context context, String email) {
        if (context != null) {
            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(USER_EMAIL, email);
            editor.apply();
        }
    }

    /**
     * Sets a USER_NAME
     * @param context
     * @param name
     */
    public static void setUserName(Context context, String name) {
        if (context != null) {
            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(USER_NAME, name);
            editor.apply();
        }
    }

    /**
     * Changes the value of LOGGED IN STATUS
     * @param context
     * @param value
     */
    public static void setLoggedInStatus(Context context, boolean value) {
        if (context != null) {
            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putBoolean(LOGGED_IN, value);
            editor.apply();
        }
    }

    /**
     * Changes the value of LOCATION_SERVICE
     * @param context
     * @param value
     */
    public static void setLocationService(Context context, boolean value) {
        if (context != null) {
            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putBoolean(LOCATION_SERVICE, value);
            editor.apply();
        }
    }

    /**
     * Changes the value of GEOFENCE
     * @param context
     * @param value
     */
    public static void setGeofence(Context context, boolean value) {
        if (context != null) {
            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putBoolean(GEOFENCE, value);
            editor.apply();
        }
    }

    /**
     * Returns the user email
     * @param context
     * @return
     */
    public static String getUserEmail(Context context) {
        return getSharedPreferences(context).getString(USER_EMAIL, "");
    }

    /**
     * Returns user name
     * @param context
     * @return
     */
    public static String getUserName(Context context) {
        if (context != null) {
            return getSharedPreferences(context).getString(USER_NAME, "");
        }
        return null;
    }

    /**
     * Checks if a user is logged in or not
     * @param context
     * @return
     */
    public static boolean getLoggedInValue(Context context) {
        return context != null && getSharedPreferences(context).getBoolean(LOGGED_IN, false);
    }

    /**
     * Returns the value of LOCATION_SERVICE
     * @param context
     * @return
     */
    public static boolean getLocationServiceValue(Context context) {
        return context != null && getSharedPreferences(context).getBoolean(LOCATION_SERVICE, false);
    }

    /**
     * Returns the value of GEOFENCE
     * @param context
     * @return
     */
    public static boolean getGeofenceValue(Context context) {
        return context != null && getSharedPreferences(context).getBoolean(GEOFENCE, false);
    }
}
