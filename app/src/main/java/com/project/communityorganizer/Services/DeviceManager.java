package com.project.communityorganizer.Services;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by
 * @author seshagiri on 25/3/15.
 */
public class DeviceManager {
    /**
     * Returns the Phone UID
     * @return String
     */
    public String getDeviceId(Context context) {
        String deviceId;
        final TelephonyManager mTelephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);

        if (mTelephony.getDeviceId() != null) deviceId = mTelephony.getDeviceId();
        else deviceId = Settings.Secure.getString(
                context.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return deviceId;
    }

    /**
     * Retrieves the carrier ID of the phone
     * @return String
     */
    public String getCarrier(Context context) {
        TelephonyManager tManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tManager.getNetworkOperatorName();
    }

    /**
     * Retrieves the phone number in use
     * @return String
     */
    public String getPhoneNumber(Context context) {
        TelephonyManager tManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tManager.getLine1Number();
    }

    /**
     * Checks if there is internet connection available
     * @param context
     * @return
     */
    public boolean isNetworkOnline(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

    /**
     * Checks if the device is connected to internet through Wifi network
     *
     * @param context
     * @return
     */
    public boolean haveConnectedWifi(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                    if (ni.isConnected()) {
                        status = true;
                        Log.v("WIFI CONNECTION ", "AVAILABLE");
                    } else {
                        Log.v("WIFI CONNECTION ", "NOT AVAILABLE");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

    /**
     * Checks if the device is connected to internet though mobile network
     *
     * @param context
     * @return
     */
    public boolean haveConnectedMobile(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                    if (ni.isConnected()) {
                        status = true;
                        Log.v("MOBILE CONNECTION ", "AVAILABLE");
                    } else {
                        Log.v("MOBILE CONNECTION ", "NOT AVAILABLE");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

    /**
     * Check if the phone is connected to internet
     */
    public void showConnectivity(Context context) {
        CharSequence notConnected = "You're not connected to internet";
        int duration = Toast.LENGTH_SHORT;

        if (!isNetworkOnline(context)) {
            Toast toast = Toast.makeText(context, notConnected, duration);
            toast.show();
        }
    }
}
