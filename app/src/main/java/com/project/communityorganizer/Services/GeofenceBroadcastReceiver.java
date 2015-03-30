package com.project.communityorganizer.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.project.communityorganizer.JSON.models.LocationJSONModel;
import com.project.communityorganizer.R;
import com.project.communityorganizer.sqlite.models.GeofenceModel;
import com.project.communityorganizer.sqlite.models.Location;

import java.util.Calendar;
import java.util.List;

/**
 * Created by
 * @author seshagiri on 23/3/15.
 */
public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    Context context;
    /**
     * {@inheritDoc}
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String action = intent.getAction();
        Log.d("GEO BROADCAST", "Received a broadcast for " + action);
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        android.location.Location location = event.getTriggeringLocation();
        sendServerUpdates(event, location);

    }

    private void sendServerUpdates(GeofencingEvent event, android.location.Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        GeofenceModel geofenceModel = GeofenceModel.getGeofenceModelLatLong(latitude, longitude);
        float accuracy = 0;
        if (location.hasAccuracy()) accuracy = location.getAccuracy();

        LocationJSONModel locationJSONModel = new LocationJSONModel();
        locationJSONModel.setDate_time(Calendar.getInstance().getTime());
        locationJSONModel.setTransition_type(event.getGeofenceTransition());
        locationJSONModel.setEmail(SaveSharedPreference.getUserEmail(context));
        locationJSONModel.setGid(geofenceModel.getGid());
        locationJSONModel.setAccuracy(accuracy);

        DeviceManager deviceManager = new DeviceManager();
        if (deviceManager.isNetworkOnline(context)) {
            // TODO implement polling algorithm here
            RestService restService = new RestService();
            List<Location> locationList = Location.getAllLocations();
            if (locationList != null) {
                ActiveAndroid.beginTransaction();
                try {
                    for (Location location1: locationList) {
                        LocationJSONModel locationJSONModel1 = Location.getJSONModel(location1);
                        restService.updateUserLocationActivity(locationJSONModel1);
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
            }
            restService.updateUserLocationActivity(locationJSONModel);
        }

        String transition = mapTransition(event.getGeofenceTransition());
        sendNotification(transition, geofenceModel.getFence_name());
    }

    /**
     * Posts a notification in the notification bar when a transition is
     * detected. If the user clicks the notification, control goes to the main
     * Activity.
     *
     * @param transitionType
     *            The type of transition that occurred.
     *
     */
    private void sendNotification(String transitionType, String locationName) {
        long when = System.currentTimeMillis();
        /*
        Intent notificationIntent = new Intent(context, LocationActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(LocationActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        */
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(transitionType)
                        .setContentText(locationName)
                        .setContentText("Geofence Transition")
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setWhen(when)
                        .build();
        mNotificationManager.notify(0, notification);
    }
    /**
     * Returns the map transaction event text
     * @param event
     * @return
     */
    private String mapTransition(int event) {
        switch (event) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "ENTER";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "EXIT";
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                return "DWELL";
            default:
                return "UNKNOWN";
        }
    }
}