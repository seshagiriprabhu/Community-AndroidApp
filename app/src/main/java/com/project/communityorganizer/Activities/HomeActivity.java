package com.project.communityorganizer.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.project.communityorganizer.Adapters.CustomListAdapter;
import com.project.communityorganizer.R;

/**
 * Created by
 * @author Seshagiri on 22/2/15.
 */
public class HomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String[] itemName = new String[]{
                "Friends",
                "Events",
                "Location",
                "Settings"
        };

        final String[] itemDescription = new String[]{
                "View friend list",
                "Create and view events",
                "See the recent locations you've visited",
                "App settings"
        };

        final Integer[] imgId = new Integer[]{
                R.drawable.ic_action_friend,
                R.drawable.ic_action_event,
                R.drawable.ic_action_geofence,
                R.drawable.ic_action_settings
        };

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ListView listView = (ListView) findViewById(android.R.id.list);
        CustomListAdapter adapter = new CustomListAdapter(this, itemName, imgId, itemDescription);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(HomeActivity.this, FriendList.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(HomeActivity.this, EventCreation.class);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(HomeActivity.this, Location.class);
                    startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(HomeActivity.this, Settings.class);
                    startActivity(intent);
                }
            }
        });
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Community Organizer");
            actionBar.setLogo(R.drawable.ic_action_logo);
        }
    }
}
