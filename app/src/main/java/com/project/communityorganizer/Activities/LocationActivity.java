package com.project.communityorganizer.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import rx.Observable;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


import com.activeandroid.ActiveAndroid;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.project.communityorganizer.R;
import com.project.communityorganizer.Services.GeofenceTransitionsIntentService;
import com.project.communityorganizer.Services.SaveSharedPreference;
import com.project.communityorganizer.sqlite.models.GeofenceModel;
import com.project.communityorganizer.utils.AddressToStringFunc;
import com.project.communityorganizer.utils.DetectedActivityToString;
import com.project.communityorganizer.utils.DisplayTextOnViewAction;
import com.project.communityorganizer.utils.LocationToStringFunc;
import com.project.communityorganizer.utils.ToMostProbableActivity;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.List;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;


/**
 * Created by
 * @author Seshagiri on 24/2/15.
 */
public class LocationActivity extends Activity
        implements View.OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<Status> {

    // Internal List of Geofence objects. In a real app, these might be provided by an API based on
    // locations within the user's proximity.
    ArrayList<Geofence> mGeofenceList;

    private static final String TAG = "Location Activity";

    // Stores the PendingIntent used to request geofence monitoring.
    private PendingIntent mGeofenceRequestIntent;
    private GoogleApiClient mApiClient;
    private ReactiveLocationProvider locationProvider;

    private TextView lastKnownLocationView;
    private TextView updatableLocationView;
    private TextView addressLocationView;
    private TextView currentActivityView;
    private Button addGeofence, removeGeofence;

    private Observable<Location> lastKnownLocationObservable;
    private Observable<Location> locationUpdatesObservable;
    private Observable<ActivityRecognitionResult> activityObservable;

    private Subscription lastKnownLocationSubscription;
    private Subscription updatableLocationSubscription;
    private Subscription addressSubscription;
    private Subscription activitySubscription;

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        setupActionBar();
        findViewsByID();
        buildGoogleApiClient();
        btnClick();
        mGeofenceRequestIntent = null;

        locationProvider = new ReactiveLocationProvider(getApplicationContext());
        lastKnownLocationObservable = locationProvider.getLastKnownLocation();
        locationUpdatesObservable = locationProvider.getUpdatedLocation(
                LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                        .setNumUpdates(5)
                        .setInterval(10000)
                        .setFastestInterval(5000)
        );
        activityObservable = locationProvider.getDetectedActivity(50);
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        Log.i(TAG, "Connection suspended");

        // onConnected() will be called again automatically when the service reconnects
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the LocationServices API.
     */
    private void buildGoogleApiClient() {
        // Rather than displayng this activity, simply display a toast indicating that the geofence
        // service is being created. This should happen in less than a second.
        if (!isGooglePlayServicesAvailable()) {
            Log.e(TAG, "Google Play services unavailable.");
            finish();
            return;
        }

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onStart() {
        super.onStart();
        lastKnownLocationSubscription = lastKnownLocationObservable
                .map(new LocationToStringFunc())
                .subscribe(
                        new DisplayTextOnViewAction(lastKnownLocationView),
                        new ErrorHandler());

        updatableLocationSubscription = locationUpdatesObservable
                .map(new LocationToStringFunc())
                .map(new Func1<String, String>() {
                    int count = 0;
                    /**
                     * {@inheritDoc}
                     * @param s
                     * @return
                     */
                    @Override
                    public String call(String s) {
                        return s + " " + count++;
                    }
                })
                .subscribe(new DisplayTextOnViewAction(updatableLocationView), new ErrorHandler());

        addressSubscription = AndroidObservable.bindActivity(this, locationUpdatesObservable
                .flatMap(new Func1<Location, Observable<List<Address>>>() {
                    /**
                     * {@inheritDoc}
                     * @param location
                     * @return
                     */
                    @Override
                    public Observable<List<Address>> call(Location location) {
                        return locationProvider
                                .getGeocodeObservable(
                                        location.getLatitude(),
                                        location.getLongitude(),
                                        1);
                    }
                })
                .map(new Func1<List<Address>, Address>() {
                    /**
                     * {@inheritDoc}
                     * @param addresses
                     * @return
                     */
                    @Override
                    public Address call(List<Address> addresses) {
                        return addresses != null && !addresses.isEmpty() ? addresses.get(0) : null;
                    }
                })
                .map(new AddressToStringFunc())
                .subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisplayTextOnViewAction(addressLocationView), new ErrorHandler());

        activitySubscription = AndroidObservable.bindActivity(this, activityObservable)
                .map(new ToMostProbableActivity())
                .map(new DetectedActivityToString())
                .subscribe(new DisplayTextOnViewAction(currentActivityView), new ErrorHandler());
        mApiClient.connect();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onStop() {
        super.onStop();
        updatableLocationSubscription.unsubscribe();
        addressSubscription.unsubscribe();
        lastKnownLocationSubscription.unsubscribe();
        activitySubscription.unsubscribe();
        mApiClient.disconnect();
    }

    /**
     * Finds view by ID
     */
    private void findViewsByID() {
        lastKnownLocationView = (TextView) findViewById(R.id.last_known_location_view);
        updatableLocationView = (TextView) findViewById(R.id.updated_location_view);
        addressLocationView = (TextView) findViewById(R.id.address_for_location_view);
        currentActivityView = (TextView) findViewById(R.id.activity_recent_view);
        addGeofence = (Button) findViewById(R.id.addGeofence);
        removeGeofence = (Button) findViewById(R.id.removeGeofence);
    }

    /**
     * Creates Action Bar
     */
    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Your Locations");
            actionBar.setLogo(R.drawable.ic_action_light_geofence);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * {@inheritDoc}
     * @param v
     */
    @Override
    public void onClick(View v) {
    }

    /**
     * Handles button click
     */
    private void btnClick() {
        if (SaveSharedPreference.getGeofenceValue(this)) {
            addGeofence.setEnabled(false);
            removeGeofence.setEnabled(true);
        } else {
            addGeofence.setEnabled(true);
            removeGeofence.setEnabled(false);
        }
        addGeofence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mApiClient.isConnected()) {
                    Toast.makeText(LocationActivity.this,
                            "Google api client not connected",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                addGeofenceButtonHandler();
                SaveSharedPreference.setGeofence(LocationActivity.this, true);
                addGeofence.setEnabled(false);
                removeGeofence.setEnabled(true);
                Toast.makeText(LocationActivity.this, "Geofence added", Toast.LENGTH_SHORT).show();
            }
        });
        removeGeofence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mApiClient.isConnected()) {
                    Toast.makeText(LocationActivity.this,
                            "Google api client not connected",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                removeGeofencesButtonHandler();
                SaveSharedPreference.setGeofence(LocationActivity.this, false);
                addGeofence.setEnabled(true);
                removeGeofence.setEnabled(false);
            }
        });
    }

    /**
     * Adds geofences, which sets alerts to be notified when the device enters or exits one of the
     * specified geofences. Handles the success or failure results returned by addGeofences().
     */
    private void addGeofenceButtonHandler() {
        if (!mApiClient.isConnected()) {
            Toast.makeText(this, "Google Api client not connect", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }


    /**
     * Removes geofences, which stops further notifications when the device enters or exits
     * previously registered geofences.
     */
    public void removeGeofencesButtonHandler() {
        if (!mApiClient.isConnected()) {
            Toast.makeText(this, "Not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            // Remove geofences.
            LocationServices.GeofencingApi.removeGeofences(
                    mApiClient,
                    // This is the same pending intent that was used in addGeofences().
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    /**
     * Tajes care of security exceptions
     * @param securityException
     */
    private void logSecurityException(SecurityException securityException) {
        Log.e(TAG, "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }

    /**
     * Creates Geofences and then creates geofencing request
     * @return
     */
    private GeofencingRequest getGeofencingRequest() {
        mGeofenceList = new ArrayList<>();
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
     * Returns the pending intents
     * @return
     */
    public PendingIntent getGeofencePendingIntent() {
        if (mGeofenceRequestIntent != null) return mGeofenceRequestIntent;
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Checks if the phone is connected to the Google play service
     * @return
     */
    public boolean isGooglePlayServicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "Google Play services is available.");
            }
            return true;
        } else {
            Log.e(TAG, "Google Play services is unavailable.");
            return false;
        }
    }

    /**
     * Create a PendingIntent that triggers GeofenceTransitionIntentService when a geofence
     * transition occurs.
     */
    public PendingIntent getGeofenceTransitionPendingIntent() {
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * {@inheritDoc}
     * @param status
     */
    @Override
    public void onResult(Status status) {

    }


    /**
     * Error Handling class
     */
    private class ErrorHandler implements Action1<Throwable> {
        @Override
        public void call(Throwable throwable) {
            Toast.makeText(LocationActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
            Log.i("MainActivity", "Error occurred", throwable);
        }
    }
}
