package com.project.communityorganizer.Services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.project.communityorganizer.sqlite.models.GeofenceModel;

import java.util.ArrayList;
import java.util.List;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import pl.charmas.android.reactivelocation.observables.geofence.AddGeofenceResult;
import pl.charmas.android.reactivelocation.observables.geofence.RemoveGeofencesResult;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by
 * @author seshagiri on 22/3/15.
 */
public class LocationService {

    private ReactiveLocationProvider locationProvider;
    Context context;

    /**
     * Constructor
     * @param context
     */
    public LocationService (Context context) {
        this.context = context;
        locationProvider = new ReactiveLocationProvider(context);
    }

    /**
     * A Function to start the location service
     */
    public void startLocationService(Context context) {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) ==
                ConnectionResult.SUCCESS) {
            addGeofence();
            SaveSharedPreference.setLocationService(context, true);
        } else {
            toast("Google play service is not enabled in your phone");
        }
    }

    /**
     * Create Geofence notification
     * @return
     */
    private PendingIntent createNotificationBroadcastPendingIntent() {
        Intent intent = new Intent(context, GeofenceBroadcastReceiver.class);
        return PendingIntent.getBroadcast(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * A function to add geofence and start location service
     */
    private void addGeofence() {
        final GeofencingRequest geofencingRequest = createGeofenceRequest();
        if (geofencingRequest == null) return;
        final PendingIntent pendingIntent = createNotificationBroadcastPendingIntent();
        locationProvider
                .removeGeofences(pendingIntent)
                .flatMap(new Func1<RemoveGeofencesResult.PendingIntentRemoveGeofenceResult,
                        Observable<AddGeofenceResult>>() {
                    /**
                     * {@inheritDoc}
                     * @param pendingIntentRemoveGeofenceResult
                     * @return
                     */
                    @Override
                    public Observable<AddGeofenceResult> call(
                            RemoveGeofencesResult.PendingIntentRemoveGeofenceResult
                                    pendingIntentRemoveGeofenceResult) {
                        return locationProvider.addGeofences(pendingIntent, geofencingRequest);
                    }
                })
                .subscribe(new Action1<AddGeofenceResult>() {
                    /**
                     * {@inheritDoc}
                     * @param addGeofenceResult
                     */
                    @Override
                    public void call(AddGeofenceResult addGeofenceResult) {
                        toast("Location service started");
                        Log.i("LOCATION SERVICE ", "service started");
                    }
                }, new Action1<Throwable>() {
                    /**
                     * {@inheritDoc}
                     * @param throwable
                     */
                    @Override
                    public void call(Throwable throwable) {
                        toast("Error starting location service");
                        Log.i("LOCATION SERVICE", "Error starting location service", throwable);
                    }
                });
    }

    /**
     * A function to create Geofences and Geofence monitoring request
     * @return
     */
    private GeofencingRequest createGeofenceRequest() {
        ArrayList<Geofence> mGeofenceList = new ArrayList<>();
        List<GeofenceModel> geofenceModelList = GeofenceModel.getAllGeofenceDetails();
        ActiveAndroid.beginTransaction();
        try {
            for (GeofenceModel geofenceModel : geofenceModelList) {
                Geofence geofence = new Geofence.Builder()
                        .setRequestId(String.valueOf(geofenceModel.getGid()))
                        .setCircularRegion(
                                geofenceModel.getLatitude(),
                                geofenceModel.getLongitude(),
                                geofenceModel.getGeofence_radius()
                        )
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setLoiteringDelay(5)
                        .setTransitionTypes(
                                Geofence.GEOFENCE_TRANSITION_ENTER |
                                        Geofence.GEOFENCE_TRANSITION_EXIT |
                                        Geofence.GEOFENCE_TRANSITION_DWELL
                        )
                        .build();
                mGeofenceList.add(geofence);
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    /**
     * To toast a message
     * @param text
     */
    private void toast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * A function to stop the location service
     */
    public void stopLocationService(Context context) {
        SaveSharedPreference.setLocationService(context, false);
        clearGeofence();
    }
    /**
     * A function to remove geofence
     */
    private void clearGeofence() {
        locationProvider.removeGeofences(createNotificationBroadcastPendingIntent())
                .subscribe(new Action1<RemoveGeofencesResult.PendingIntentRemoveGeofenceResult>() {
                    /**
                     * {@inheritDoc}
                     * @param pendingIntentRemoveGeofenceResult
                     */
                    @Override
                    public void call(RemoveGeofencesResult.PendingIntentRemoveGeofenceResult
                                             pendingIntentRemoveGeofenceResult) {
                        toast("Location Service stopped");
                        Log.i("LOCATION SERVICE", "Location service stopped");
                    }
                }, new Action1<Throwable>() {
                    /**
                     * {@inheritDoc}
                     * @param throwable
                     */
                    @Override
                    public void call(Throwable throwable) {
                        toast("Error Removing Geofences");
                        Log.i("Location Service", "Error removing geofences", throwable);
                    }
                });
    }
}
