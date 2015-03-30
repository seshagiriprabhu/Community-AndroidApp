package com.project.communityorganizer.JSON.models;

import java.util.Date;

/**
 * Created by
 * @author seshagiri on 25/3/15.
 */
public class LocationJSONModel {
    public String email;
    public Date date_time;
    public float accuracy;
    public int transition_type, gid;

    /**
     * Default constructor
     */
    public LocationJSONModel() {
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate_time() {
        return date_time;
    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public int getTransition_type() {
        return transition_type;
    }

    public void setTransition_type(int transition_type) {
        this.transition_type = transition_type;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }
}
