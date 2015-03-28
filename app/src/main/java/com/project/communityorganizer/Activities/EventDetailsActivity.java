package com.project.communityorganizer.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.project.communityorganizer.Constants;
import com.project.communityorganizer.JSON.models.EventJSONModel;
import com.project.communityorganizer.R;
import com.project.communityorganizer.Services.SaveSharedPreference;
import com.project.communityorganizer.sqlite.models.Event;
import com.project.communityorganizer.sqlite.models.EventAttendance;
import com.project.communityorganizer.sqlite.models.Friend;
import com.project.communityorganizer.sqlite.models.GeofenceModel;

import java.util.List;

/**
 * Created by
 * @author Seshagiri on 25/2/15.
 */
public class EventDetailsActivity extends Activity {
    private EventJSONModel eventJSONModel = new EventJSONModel();
    private TextView eventName;
    private TextView eventDescription;
    private TextView eventCreator;
    private TextView personalFeeling;
    private TextView startTime;
    private TextView endTime;
    private TextView location;
    private TextView attendees;

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        setActionBar();
        findViewsById();
        setContent(savedInstanceState);
    }

    /**
     * Set Action bar for the page
     */
    private void setActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            if (eventJSONModel != null) actionBar.setTitle("Event Details");
            else actionBar.setTitle("Failed to fetch event details");
            actionBar.setLogo(R.drawable.ic_action_light_event);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Sets the content of the activity
     * @param savedInstanceState
     */
    private void setContent(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) eventJSONModel = null;
            else eventJSONModel.event_id = extras.getInt("event_id");
        } else eventJSONModel.event_id = (int) savedInstanceState.getSerializable("event_id");

        if (eventJSONModel != null) {
            Event event = Event.getEventDetails(eventJSONModel.getEvent_id());
            Friend friend = event.getEvent_creator();
            GeofenceModel geofenceModel = event.getGeofence_Model_id();

            eventJSONModel.event_creator = friend.getDisplay_name()
                    + " <" + friend.getEmail() + ">";
            eventJSONModel.setStart_time(event.getStart_time());
            eventJSONModel.setEnd_time(event.getEnd_time());

            eventName.setText(event.getEvent_name());
            eventDescription.setText(event.getEvent_description());
            personalFeeling.setText(event.getPersonal_feeling());
            startTime.setText(eventJSONModel.getStart_time_as_String());
            endTime.setText(eventJSONModel.getEnd_time_as_String());
            location.setText(geofenceModel.getFence_name()
                    + " [" + geofenceModel.getLatitude() + ", " + geofenceModel.getLongitude() + "]");
            eventCreator.setText(eventJSONModel.getEvent_creator());

            if (SaveSharedPreference
                    .getUserEmail(EventDetailsActivity.this)
                    .equals(friend.getEmail()))
                eventCreator.setText(eventJSONModel.getEvent_creator() + " (you)");

            List<EventAttendance> eventAttendanceList =
                    EventAttendance.getEventAttendeesList(event);

            if (eventAttendanceList == null) attendees.setText(Constants.NA);
            else {
                String attendeesList = "";
                int index = 1;

                ActiveAndroid.beginTransaction();
                try {
                    for (EventAttendance eventAttendance : eventAttendanceList) {
                        Friend friend1 = eventAttendance.getEmail();
                        attendeesList += index++ + ". " + friend1.getDisplay_name() +
                                " <" + friend1.getEmail() + ">";
                        if (SaveSharedPreference.getUserEmail(EventDetailsActivity.this)
                                .equals(friend1.getEmail())) attendeesList += " (you)";
                        attendeesList += " ";
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                attendees.setText(attendeesList);
            }
        } else {
            eventName.setText(Constants.NA);
            eventCreator.setText(Constants.NA);
            eventDescription.setText(Constants.NA);
            personalFeeling.setText(Constants.NA);
            startTime.setText(Constants.NA);
            endTime.setText(Constants.NA);
            location.setText(Constants.NA);
            attendees.setText(Constants.NA);
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
        attendees = (TextView) findViewById(R.id.eventDetailsAttendees);
    }

    /**
     * {@inheritDoc}
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_details, menu);
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
}
