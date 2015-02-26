package com.project.communityorganizer.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.project.communityorganizer.JSON.models.FriendJSONModel;
import com.project.communityorganizer.R;
import java.text.ParseException;

/**
 * Created by
 * @author seshagiri on 25/2/15.
 */
public class FriendDetailsActivity extends Activity {
    final Integer[] imgId = new Integer[]{
            R.mipmap.ic_launcher_male,
            R.mipmap.ic_launcher_female,
            R.drawable.ic_action_user
    };

    private ImageView profilePic;
    private TextView displayName;
    private TextView email;
    private TextView phoneNumber;
    private TextView date_of_birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_details);
        findViewsById();
        FriendJSONModel friendJSONModel = new FriendJSONModel();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                friendJSONModel = null;
            } else {
                friendJSONModel.email = extras.getString("email");
                friendJSONModel.display_name = extras.getString("display_name");
                friendJSONModel.gender = extras.getString("gender");
                friendJSONModel.phone_number = extras.getString("phone_number");
                try {
                    friendJSONModel. setDate_of_birth_from_utc(extras.getString("date_of_birth"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            friendJSONModel.email = (String) savedInstanceState.getSerializable("email");
            friendJSONModel.display_name = (String) savedInstanceState
                    .getSerializable("display_name");
            friendJSONModel.gender = (String) savedInstanceState.getSerializable("gender");
            friendJSONModel.phone_number = (String) savedInstanceState
                    .getSerializable("phone_number");
            try {
                friendJSONModel.setDate_of_birth_from_utc(
                        (String) savedInstanceState.getSerializable("date_of_birth"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (friendJSONModel != null && friendJSONModel.getGender().equals("M")) {
            profilePic.setImageResource(imgId[0]);
        } else if (friendJSONModel != null &&  friendJSONModel.getGender().equals("F")) {
            profilePic.setImageResource(imgId[1]);
        } else {
            profilePic.setImageResource(imgId[2]);
        }
        if (friendJSONModel != null) {
            displayName.setText(friendJSONModel.getDisplay_name());
            email.setText(friendJSONModel.getEmail());
            phoneNumber.setText(friendJSONModel.getPhone_number());
            date_of_birth.setText(friendJSONModel.getDate_of_birth_as_string());
        }

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            if (friendJSONModel != null) actionBar.setTitle(friendJSONModel.getDisplay_name());
            else actionBar.setTitle("Failed to fetch user data");
            actionBar.setLogo(R.drawable.ic_action_light_friend);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * A function to find views by ID in-order to assign
     */
    private void findViewsById(){
        displayName = (TextView) findViewById(R.id.friendName);
        profilePic = (ImageView) findViewById(R.id.friend_profile_image);
        email = (TextView) findViewById(R.id.friendEmail);
        phoneNumber = (TextView) findViewById(R.id.friendPhone);
        date_of_birth = (TextView) findViewById(R.id.friendDOB);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_details, menu);
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
