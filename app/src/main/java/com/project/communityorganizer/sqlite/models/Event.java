package com.project.communityorganizer.sqlite.models;

/* Android core imports */
import android.text.format.Time;

/* Active Android imports */
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/* User defined models */
import com.activeandroid.query.Select;
import com.project.communityorganizer.sqlite.models.Geofence;
import com.project.communityorganizer.sqlite.models.Friend;

/* Java imports */
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by seshagiri on 19/2/15.
 */
@Table(name="Event")
public class Event extends Model{
    @Column(index = true)
    public int event_id;

    @Column(index = true)
    public String event_name;

    @Column(name="event_description")
    public String event_description;

    @Column(name="Friend", index = true)
    public Friend event_creator;

    @Column(name="personal_feeling")
    public String personal_feeling;

    @Column(name="start_time")
    public Date start_time;

    @Column(name="end_time")
    public Date end_time;

    @Column(name="Geofence")
    public Geofence geofence_id;

    @Column(name="modified_time")
    public Date modified_time;


    /* Default Constructor */
    public Event() { super(); }

    /* Constructor for storing into DB */
    public Event(
            int event_id,
            String event_name,
            String event_description,
            String event_creator,
            String personal_feeling,
            Date start_time,
            Date end_time,
            int geofence_id,
            Date modified_time) {
        super();
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_description = event_description;
        Friend friend = Friend.getFriendDetails(event_creator);
        this.event_creator = friend;
        this.personal_feeling = personal_feeling;
        this.start_time = start_time;
        this.end_time = end_time;
        Geofence geofence = Geofence.getGeofenceDetails(geofence_id);
        this.geofence_id = geofence;
        this.modified_time = modified_time;
    }

    public Event(JSONObject json) throws JSONException, ParseException {
        this.event_id = json.getInt("event_id");
        this.event_name = json.getString("event_name");
        this.event_description = json.getString("event_description");
        String email = json.getString("event_creator");
        Friend friend = Friend.getFriendDetails(email);
        this.event_creator = friend;
        this.personal_feeling = json.getString("personal_feeling");

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        String string_start_time = json.getString("start_time");
        Date start_time = formatter.parse(string_start_time);

        String string_end_time = json.getString("end_time");
        Date end_time = formatter.parse(string_end_time);

        String string_modified_time = json.getString("modified_time");
        Date modified_time = formatter.parse(string_modified_time);

        this.start_time = start_time;
        this.end_time = end_time;
        this.modified_time = modified_time;

        int gid = json.getInt("gid");
        Geofence geofence = Geofence.getGeofenceDetails(gid);
        this.geofence_id = geofence;
    }

    public static Event getEventDetails(int event_id) {
        return new Select().from(Event.class).where("event_id = ?", event_id).executeSingle();
    }
}
