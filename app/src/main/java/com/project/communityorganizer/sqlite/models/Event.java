package com.project.communityorganizer.sqlite.models;

import java.math.BigInteger;

/**
 * Created by seshagiri on 19/2/15.
 */
public class Event {
    private BigInteger event_id;
    private String event_name;
    private String event_description;
    private String event_creator;
    private String personal_feeling;
    private String start_time;
    private String end_time;
    private BigInteger geofence_id;
    private String modified_time;

    public Event(
            BigInteger event_id,
            String event_name,
            String event_description,
            String event_creator,
            String personal_feeling,
            String start_time,
            String end_time,
            BigInteger geofence_id,
            String modified_time) {
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

    public String getEvent_creator() {
        return event_creator;
    }

    public void setEvent_creator(String event_creator) {
        this.event_creator = event_creator;
    }

    public String getPersonal_feeling() {
        return personal_feeling;
    }

    public void setPersonal_feeling(String personal_feeling) {
        this.personal_feeling = personal_feeling;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public BigInteger getGeofence_id() {
        return geofence_id;
    }

    public void setGeofence_id(BigInteger geofence_id) {
        this.geofence_id = geofence_id;
    }

    public String getModified_time() {
        return modified_time;
    }

    public void setModified_time(String modified_time) {
        this.modified_time = modified_time;
    }
}
