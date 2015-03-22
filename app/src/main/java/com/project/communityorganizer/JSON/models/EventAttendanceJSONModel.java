package com.project.communityorganizer.JSON.models;

/**
 * Created by
 * @author seshagiri on 1/3/15.
 */
public class EventAttendanceJSONModel {
    public int aid;
    public int event_id;
    public String email;
    public String status;

    public EventAttendanceJSONModel() {
        super();
    }

    public EventAttendanceJSONModel(
            int aid,
            int event_id,
            String email,
            String display_name,
            String status) {
        this.aid = aid;
        this.event_id = event_id;
        this.email = email;
        this.status = status;
    }

    public int getAid() {
        return aid;
    }

    public int getEvent_id() {
        return event_id;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}
