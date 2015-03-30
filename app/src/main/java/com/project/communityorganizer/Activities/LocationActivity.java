package com.project.communityorganizer.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import rx.Observable;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.LocationRequest;
import com.project.communityorganizer.R;
import com.project.communityorganizer.utils.AddressToStringFunc;
import com.project.communityorganizer.utils.DetectedActivityToString;
import com.project.communityorganizer.utils.DisplayTextOnViewAction;
import com.project.communityorganizer.utils.LocationToStringFunc;
import com.project.communityorganizer.utils.ToMostProbableActivity;

import java.util.List;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;


/**
 * Created by
 * @author Seshagiri on 24/2/15.
 */
public class LocationActivity extends Activity {

    private ReactiveLocationProvider locationProvider;

    private TextView lastKnownLocationView;
    private TextView updatableLocationView;
    private TextView addressLocationView;
    private TextView currentActivityView;

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
    }

    /**
     * Finds view by ID
     */
    private void findViewsByID() {
        lastKnownLocationView = (TextView) findViewById(R.id.last_known_location_view);
        updatableLocationView = (TextView) findViewById(R.id.updated_location_view);
        addressLocationView = (TextView) findViewById(R.id.address_for_location_view);
        currentActivityView = (TextView) findViewById(R.id.activity_recent_view);
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
