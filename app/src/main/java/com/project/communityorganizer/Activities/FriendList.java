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

import com.project.communityorganizer.Adapters.FriendListCursorAdaptor;
import com.project.communityorganizer.JSON.models.FriendJSONModel;
import com.project.communityorganizer.R;
import com.project.communityorganizer.Services.RestService;
import com.project.communityorganizer.Services.SaveSharedPreference;
import com.project.communityorganizer.sqlite.models.Friend;

import java.text.ParseException;

/**
 * Created by
 * @author seshagiri on 25/2/15.
 */
public class FriendList extends Activity {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Friend List");
            actionBar.setLogo(R.drawable.ic_action_light_friend);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (isConnected()) new FriendListUpdateTask().execute();

        ListView listView = (ListView) findViewById(R.id.listView);
        final Cursor cursor = Friend.fetchResultCursor();
        FriendListCursorAdaptor listCursorAdaptor = new FriendListCursorAdaptor(this, cursor);
        listView.setAdapter(listCursorAdaptor);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                FriendJSONModel friendJSONModel = new FriendJSONModel();
                friendJSONModel.display_name = cursor.getString(
                        cursor.getColumnIndexOrThrow("display_name"));
                friendJSONModel.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                friendJSONModel.gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                friendJSONModel.phone_number = cursor.getString(
                        cursor.getColumnIndexOrThrow("phone_number"));
                try {
                    friendJSONModel.set_Date_of_birth_from_epoch(cursor.getString(
                            cursor.getColumnIndexOrThrow("date_of_birth")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                intent = new Intent(FriendList.this, FriendDetailsActivity.class);
                intent.putExtra("display_name", friendJSONModel.getDisplay_name());
                intent.putExtra("email", friendJSONModel.getEmail());
                intent.putExtra("gender", friendJSONModel.getGender());
                intent.putExtra("phone_number", friendJSONModel.getPhone_number());
                intent.putExtra("date_of_birth", friendJSONModel.getDate_of_birth_as_string());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_list, menu);
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
     * Asynchronous task to fetch any new friends
     */
    private class FriendListUpdateTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(FriendList.this, "Wait", "Updating Friend List");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            final RestService restService = new RestService();
            restService.fetchFriendList(SaveSharedPreference.getUserEmail(FriendList.this));
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
