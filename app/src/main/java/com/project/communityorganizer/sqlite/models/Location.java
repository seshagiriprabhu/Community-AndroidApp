package com.project.communityorganizer.sqlite.models;

/* Active Android libs */
import android.text.format.Time;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/* Java libs */
import java.math.BigDecimal;
import java.util.Date;

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
    public Date date_time;
    public Double accuracy;
    public int transition_type;
    @Column(name = "Geofence")
    public Geofence gid;

    /* Default constructor */
    public Location() { super(); }

    /* Storing into local DB */
    public Location(
            User email,
            Date date_time,
            Double accuracy,
            int transition_type,
            int gid) {
        super();
        this.email = email;
        this.date_time = date_time;
        this.accuracy = accuracy;
        this.transition_type = transition_type;
        Geofence geofence = Geofence.getGeofenceDetails(gid);
        this.gid = geofence;
    }
}
