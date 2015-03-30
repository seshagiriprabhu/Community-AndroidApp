package com.project.communityorganizer.utils;
import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.location.GeofenceStatusCodes;

/**
 * Created by
 * @author seshagiri on 25/3/15.
 */
public class GeofenceErrorMessages {
    /**
     * Prevents instantiation.
     */
    private GeofenceErrorMessages() {}

    /**
     * Returns the error string for a geofencing error code.
     */
    public static String getErrorString(Context context, int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "Geofence Not Available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too Many Geofences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown geofence error";
        }
    }
}