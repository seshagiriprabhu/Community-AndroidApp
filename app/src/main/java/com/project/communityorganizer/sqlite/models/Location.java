package com.project.communityorganizer.sqlite.models;

/* Active Android libs */
import android.text.format.Time;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/* Java libs */
import java.math.BigDecimal;

/* User defined models */
import com.project.communityorganizer.sqlite.models.User;
import com.project.communityorganizer.sqlite.models.Geofence;


/**
 * Created by seshagiri on 19/2/15.
 */
@Table(name="Location")
public class Location extends Model{
    @Column(name = "User", index = true)
    public User email;
    public Time date_time;
    public BigDecimal accuracy;
    public Integer transition_type;
    @Column(name = "Geofence")
    public Geofence gid;

    /* Default constructor */
    public Location() { super(); }

    /* Storing into local DB */
    public Location(
            User email,
            Time date_time,
            BigDecimal accuracy,
            Integer transition_type,
            Geofence gid) {
        super();
        this.email = email;
        this.date_time = date_time;
        this.accuracy = accuracy;
        this.transition_type = transition_type;
        this.gid = gid;
    }

    public User getEmail() {
        return email;
    }

    public void setEmail(User email) {
        this.email = email;
    }

    public Time getDate_time() {
        return date_time;
    }

    public void setDate_time(Time date_time) {
        this.date_time = date_time;
    }

    public BigDecimal getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(BigDecimal accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getTransition_type() {
        return transition_type;
    }

    public void setTransition_type(Integer transition_type) {
        this.transition_type = transition_type;
    }

    public Geofence getGid() {
        return gid;
    }

    public void setGid(Geofence gid) {
        this.gid = gid;
    }
}
