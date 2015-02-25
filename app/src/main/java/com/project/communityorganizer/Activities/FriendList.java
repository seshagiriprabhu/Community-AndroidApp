package com.project.communityorganizer.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.project.communityorganizer.Adapters.FriendListCursorAdaptor;
import com.project.communityorganizer.R;
import com.project.communityorganizer.Services.RestService;
import com.project.communityorganizer.Services.SaveSharedPreference;
import com.project.communityorganizer.sqlite.models.Friend;

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
        }

        if (isConnected()) new FriendListUpdateTask().execute();

        ListView listView = (ListView) findViewById(R.id.listView);
        Cursor cursor = Friend.fetchResultCursor();
        FriendListCursorAdaptor listCursorAdaptor = new FriendListCursorAdaptor(this, cursor);
        listView.setAdapter(listCursorAdaptor);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
