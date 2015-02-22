package com.project.communityorganizer.sqlite.models;

/* Active Android libs */
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/* User defined models */
import com.project.communityorganizer.sqlite.models.Event;
import com.project.communityorganizer.sqlite.models.Friend;

/* Java libs */
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.text.ParseException;

/**
 * Created by seshagiri on 19/2/15.
 */
@Table(name="EventAttendance")
public class EventAttendance extends Model{
    @Column(unique = true)
    public int aid;

    @Column(name="Event")
    public Event event_id;

    @Column(name="Friend")
    public Friend email;
    public String display_name;
    public String status;

    /* Default Constructor */
    public EventAttendance() { super(); }

    /* Detailed Constructor */
    public EventAttendance(
            int aid,
            int event_id,
            String email,
            String display_name,
            String status) {
        super();
        this.aid = aid;
        Event event = Event.getEventDetails(event_id);
        this.event_id = event;
        Friend friend = Friend.getFriendDetails(email);
        this.email = friend;
        this.display_name = display_name;
        this.status = status;
    }

    public EventAttendance(JSONObject json) throws JSONException, ParseException {
        this.aid = json.getInt("aid");
        int event_id = json.getInt("event_id");

        Event event = Event.getEventDetails(event_id);
        this.event_id = event;

        String email = json.getString("email");
        Friend friend = Friend.getFriendDetails(email);
        this.email = friend;

        this.display_name = json.getString("display_name");;
        this.status = json.getString("status");
    }
}
