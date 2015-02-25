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

    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUserEmail(Context context, String email) {
        if (context != null) {
            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(USER_EMAIL, email);
            editor.apply();
        }
    }

    public static void setUserName(Context context, String name) {
        if (context != null) {
            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(USER_NAME, name);
            editor.apply();
        }
    }

    public static String getUserEmail(Context context) {
        if (context != null) {
            return getSharedPreferences(context).getString(USER_EMAIL, "");
        }
        return null;
    }

    public static String getUserName(Context context) {
        if (context != null) {
            return getSharedPreferences(context).getString(USER_NAME, "");
        }
        return null;
    }
}
