package com.project.communityorganizer.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.project.communityorganizer.Constants;
import com.project.communityorganizer.JSON.models.EventJSONModel;
import com.project.communityorganizer.R;
import com.project.communityorganizer.Services.SaveSharedPreference;
import com.project.communityorganizer.sqlite.models.Friend;
import com.project.communityorganizer.sqlite.models.Geofence;
import java.text.ParseException;

/**
 * Created by
 * @author Seshagiri on 25/2/15.
 */
public class EventDetailsActivity extends Activity {
    private TextView eventName;
    private TextView eventDescription;
    private TextView eventCreator;
    private TextView personalFeeling;
    private TextView startTime;
    private TextView endTime;
    private TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        findViewsById();
        EventJSONModel eventJSONModel = new EventJSONModel();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                eventJSONModel = null;
            } else {
                eventJSONModel.event_creator = extras.getString("event_creator");
                eventJSONModel.event_name = extras.getString("event_name");
                eventJSONModel.personal_feeling = extras.getString("personal_feeling");
                eventJSONModel.geofence_id = extras.getInt("geofence_id");
                try {
                    eventJSONModel.setStart_time2(extras.getString("start_time"));
                    eventJSONModel.setEnd_time2(extras.getString("end_time"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            eventJSONModel.event_name = (String) savedInstanceState
                    .getSerializable("event_name");


            eventJSONModel.event_description = (String) savedInstanceState
                    .getSerializable("event_description");


            eventJSONModel.personal_feeling = (String) savedInstanceState
                    .getSerializable("personal_feeling");


            eventJSONModel.event_creator = (String) savedInstanceState
                    .getSerializable("event_creator");
            eventJSONModel.geofence_id = (int) savedInstanceState
                    .getSerializable("geofence_id");

            try {
                eventJSONModel.setStart_time2((String) savedInstanceState
                        .getSerializable("start_time"));
                eventJSONModel.setEnd_time2((String) savedInstanceState
                        .getSerializable("end_time"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (eventJSONModel != null) {
            eventName.setText(eventJSONModel.getEvent_name());
            eventDescription.setText(eventJSONModel.getEvent_description());

            Friend friend = Friend.getFriendIdDetails(Long.valueOf(eventJSONModel.getEvent_creator()));
            if (!SaveSharedPreference.getUserEmail(EventDetailsActivity.this).equals(friend.getEmail()))
                eventCreator.setText(friend.getDisplay_name());
            else eventCreator.setText(friend.getDisplay_name() + " (you)");
            friend.save();

            personalFeeling.setText(eventJSONModel.getPersonal_feeling());

            startTime.setText(eventJSONModel.getStart_time_as_String());
            endTime.setText(eventJSONModel.getEnd_time_as_String());

            Geofence geofence = Geofence.getGeofenceDetails(eventJSONModel.getGeofence_id());
            location.setText(geofence.getFence_name()
                    + " [" + geofence.getLatitude() + ", " + geofence.getLongitude() + "]");
        } else {
            eventName.setText(Constants.NA);
            eventCreator.setText(Constants.NA);
            eventDescription.setText(Constants.NA);
            personalFeeling.setText(Constants.NA);
            startTime.setText(Constants.NA);
            endTime.setText(Constants.NA);
            location.setText(Constants.NA);
        }

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            if (eventJSONModel != null) actionBar.setTitle(eventJSONModel.getEvent_name());
            else actionBar.setTitle("Failed to fetch event details");
            actionBar.setLogo(R.drawable.ic_action_light_event);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * A function to find views by ID in-order to assign
     */
    public void findViewsById() {
        eventName = (TextView) findViewById(R.id.eventDetailsName);
        eventDescription = (TextView) findViewById(R.id.eventDescription);
        eventCreator = (TextView) findViewById(R.id.eventCreator);
        startTime = (TextView) findViewById(R.id.startTime);
        endTime = (TextView) findViewById(R.id.endTime);
        location = (TextView) findViewById(R.id.location);
        personalFeeling = (TextView) findViewById(R.id.personalFeeling);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_details, menu);
        return true;
    }

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
}
