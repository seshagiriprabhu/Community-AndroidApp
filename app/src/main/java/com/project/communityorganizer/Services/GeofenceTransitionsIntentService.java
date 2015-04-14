package com.project.communityorganizer.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.project.communityorganizer.Activities.LocationActivity;
import com.project.communityorganizer.JSON.models.LocationJSONModel;
import com.project.communityorganizer.R;
import com.project.communityorganizer.sqlite.models.Friend;
import com.project.communityorganizer.sqlite.models.GeofenceModel;
import com.project.communityorganizer.utils.GeofenceErrorMessages;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by
 * @author seshagiri on 13/4/15.
 */
public class GeofenceTransitionsIntentService extends IntentService {

    private static final String TAG = "Geofence";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public GeofenceTransitionsIntentService() {
        super(GeofenceTransitionsIntentService.class.getSimpleName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Handles incoming intents.
     * @param intent sent by Location Services. This Intent is provided to Location
     *               Services (inside a PendingIntent) when addGeofences() is called.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );
            android.location.Location location = geofencingEvent.getTriggeringLocation();
            storeLocationUpdatesInDB(geofencingEvent, location);
            // Send notification and log the transition details.
            sendNotification(geofenceTransitionDetails);
            Log.i(TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
            Log.e(TAG, "Error");
        }
    }

    /**
     * Posts a notification in the notification bar when a transition is detected.
     * If the user clicks the notification, control goes to the MainActivity.
     */
    private void sendNotification(String notificationDetails) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(getApplicationContext(), LocationActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(LocationActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.ic_launcher)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText(notificationDetails)
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }

    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param context               The app context.
     * @param geofenceTransition    The ID of the geofence transition.
     * @param triggeringGeofences   The geofence(s) triggered.
     * @return                      The transition details formatted as String.
     */
    private String getGeofenceTransitionDetails(
            Context context,
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);
        GeofenceModel geofenceModel = GeofenceModel.getGeofenceDetails(
        Integer.valueOf(triggeringGeofencesIdsString));

        return geofenceTransitionString + ": " +
                geofenceModel.getFence_name() +
                "[" + geofenceModel.getLongitude() + ", " + geofenceModel.getLatitude() + "]";
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType    A transition type constant defined in Geofence
     * @return                  A String indicating the type of transition
     */
    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "Enter";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "Exit";
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                return "Dwell";
            default:
                return "Unknown";
        }
    }

    /**
     * Stores the location updates in a local db
     * @param event
     * @param location
     */
    private void storeLocationUpdatesInDB(
            GeofencingEvent event,
            android.location.Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        GeofenceModel geofenceModel = GeofenceModel.getGeofenceModelLatLong(latitude, longitude);
        float accuracy = 0;
        if (location.hasAccuracy()) accuracy = location.getAccuracy();

        Friend friend = Friend.getLoggedInUser();

        LocationJSONModel locationJSONModel = new LocationJSONModel();
        locationJSONModel.setDate_time(Calendar.getInstance().getTime());
        locationJSONModel.setTransition_type(event.getGeofenceTransition());
        locationJSONModel.setEmail(friend.getEmail());
        locationJSONModel.setGid(geofenceModel.getGid());
        locationJSONModel.setAccuracy(accuracy);
    }
}
