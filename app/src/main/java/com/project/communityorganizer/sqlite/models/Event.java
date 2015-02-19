package com.project.communityorganizer.sqlite.models;

/* Android core imports */
import android.text.format.Time;

/* Active Android imports */
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/* User defined models */
import com.project.communityorganizer.sqlite.models.Geofence;
import com.project.communityorganizer.sqlite.models.Friend;

/* Java imports */
import java.math.BigInteger;

/**
 * Created by seshagiri on 19/2/15.
 */
@Table(name="Event")
public class Event extends Model{
    @Column(index = true)
    public BigInteger event_id;

    @Column(index = true)
    public String event_name;
    public String event_description;

    @Column(name="Friend", index = true)
    public Friend event_creator;
    public String personal_feeling;
    public Time start_time;
    public Time end_time;

    @Column(name="Geofence")
    public Geofence geofence_id;
    public Time modified_time;


    /* Default Constructor */
    public Event() { super(); }

    /* Constructor for storing into DB */
    public Event(
            BigInteger event_id,
            String event_name,
            String event_description,
            Friend event_creator,
            String personal_feeling,
            Time start_time,
            Time end_time,
            Geofence geofence_id,
            Time modified_time) {
        super();
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_description = event_description;
        this.event_creator = event_creator;
        this.personal_feeling = personal_feeling;
        this.start_time = start_time;
        this.end_time = end_time;
        this.geofence_id = geofence_id;
        this.modified_time = modified_time;
    }

    public BigInteger getEvent_id() {
        return event_id;
    }

    public void setEvent_id(BigInteger event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public void setEvent_creator(Friend event_creator) {
        this.event_creator = event_creator;
    }

    public String getPersonal_feeling() {
        return personal_feeling;
    }

    public void setPersonal_feeling(String personal_feeling) {
        this.personal_feeling = personal_feeling;
    }

    public Time getStart_time() {
        return start_time;
    }

    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }

    public Time getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Time end_time) {
        this.end_time = end_time;
    }

    public Geofence getGeofence_id() {
        return geofence_id;
    }

    public void setGeofence_id(Geofence geofence_id) {
        this.geofence_id = geofence_id;
    }

    public Time getModified_time() {
        return modified_time;
    }

    public void setModified_time(Time modified_time) {
        this.modified_time = modified_time;
    }
}
