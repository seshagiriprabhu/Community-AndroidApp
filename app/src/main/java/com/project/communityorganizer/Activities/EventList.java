package com.project.communityorganizer.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.project.communityorganizer.Adapters.EventListCursorAdaptor;
import com.project.communityorganizer.JSON.models.EventJSONModel;
import com.project.communityorganizer.R;
import com.project.communityorganizer.Services.RestService;
import com.project.communityorganizer.sqlite.models.Event;

import java.text.ParseException;

/**
 * Created by
 * @author Seshagiri on 25/2/15.
 */
public class EventList extends Activity {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Event List");
            actionBar.setLogo(R.drawable.ic_action_light_event);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (isConnected()) new EventListUpdatetask().execute();

        ListView listView = (ListView) findViewById(R.id.eventList);
        final Cursor cursor = Event.fetchResultCursor();
        EventListCursorAdaptor eventListCursorAdaptor = new EventListCursorAdaptor(this, cursor);
        listView.setAdapter(eventListCursorAdaptor);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                EventJSONModel eventJSONModel = new EventJSONModel();
                eventJSONModel.event_creator = cursor.getString(
                        cursor.getColumnIndexOrThrow("Friend"));
                eventJSONModel.event_name = cursor.getString(
                        cursor.getColumnIndexOrThrow("event_name"));
                eventJSONModel.event_description = cursor
                        .getString(cursor.getColumnIndexOrThrow("event_description"));
                eventJSONModel.personal_feeling = cursor
                        .getString(cursor.getColumnIndexOrThrow("personal_feeling"));
                eventJSONModel.geofence_id = Integer.parseInt(cursor
                        .getString(cursor.getColumnIndexOrThrow("Geofence")));
                try {
                    eventJSONModel.setStart_time_from_epoch(
                            cursor.getString(cursor.getColumnIndexOrThrow("start_time")));
                    eventJSONModel.setEnd_time_from_epoch(
                            cursor.getString(cursor.getColumnIndexOrThrow("end_time")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                intent = new Intent(EventList.this, EventDetailsActivity.class);
                intent.putExtra("event_name", eventJSONModel.getEvent_name());
                intent.putExtra("event_description", eventJSONModel.getEvent_description());
                intent.putExtra("personal_feeling", eventJSONModel.getPersonal_feeling());
                intent.putExtra("event_creator", eventJSONModel.getEvent_creator());
                intent.putExtra("start_time", eventJSONModel.getStart_time_as_String());
                intent.putExtra("end_time", eventJSONModel.getEnd_time_as_String());
                intent.putExtra("geofence_id", eventJSONModel.getGeofence_id());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_list, menu);
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

    /**
     * Asynchronous task to fetch any new events
     */
    public class EventListUpdatetask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(EventList.this, "Wait", "Updating Event List");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            final RestService restService = new RestService();
            restService.fetchOpenEventList();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) progressDialog.dismiss();

        }
    }

    /**
     *  Checks if the device is connected to internet
     *  @return boolean
     */
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
