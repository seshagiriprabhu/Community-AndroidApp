package com.project.communityorganizer.sqlite.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.project.communityorganizer.JSON.models.LocationJSONModel;

import java.util.Date;
import java.util.List;

/**
 * Created by
 * @author seshagiri on 19/2/15.
 */
@Table(name="Location")
public class Location extends Model{
    @Column(name = "User", index = true)
    public User email;

    @Column(name="date_time")
    public Date date_time;

    @Column(name="accuracy")
    public Float accuracy;

    @Column(name="transition_type")
    public int transition_type;

    @Column(name = "Geofence")
    public GeofenceModel gid;

    /* Default constructor */
    public Location() { super(); }

    /**
     * Storing into local DB
      * @param email
     * @param date_time
     * @param accuracy
     * @param transition_type
     * @param gid
     */
    public Location(
            User email,
            Date date_time,
            Float accuracy,
            int transition_type,
            int gid) {
        super();
        this.email = email;
        this.date_time = date_time;
        this.accuracy = accuracy;
        this.transition_type = transition_type;
        GeofenceModel geofenceModel = GeofenceModel.getGeofenceDetails(gid);
        this.gid = geofenceModel;
    }

    /**
     * Storing into DB using a JSON model
     * @param model
     */
    public Location(LocationJSONModel model) {
        this.setEmail(User.getUserObject(model.getEmail()));
        this.setDate_time(model.getDate_time());
        this.setAccuracy(model.getAccuracy());
        this.setGid(GeofenceModel.getGeofenceDetails(model.getGid()));
        this.setTransition_type(model.getTransition_type());
    }

    public User getEmail() {
        return email;
    }

    public void setEmail(User email) {
        this.email = email;
    }

    public Date getDate_time() {
        return date_time;
    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }

    public int getTransition_type() {
        return transition_type;
    }

    public void setTransition_type(int transition_type) {
        this.transition_type = transition_type;
    }

    public GeofenceModel getGid() {
        return gid;
    }

    public void setGid(GeofenceModel gid) {
        this.gid = gid;
    }

    /**
     * Returns the location object of an event at a given time
     * @param date
     * @return
     */
    public static Location getLocationObject(Date date) {
        return new Select().from(Location.class).where("date_time = ?", date).executeSingle();
    }

    /**
     * Returns all the entries in the list
     * @return List
     */
    public static List<Location> getAllLocations() {
        return new Select().from(Location.class).execute();
    }

    /**
     * Returns JSON model of a given Active Android Object
     * @param location
     * @return
     */
    public static LocationJSONModel getJSONModel(Location location) {
        LocationJSONModel locationJSONModel = new LocationJSONModel();
        locationJSONModel.setEmail(location.getEmail().getEmail());
        locationJSONModel.setGid(location.getGid().getGid());
        locationJSONModel.setAccuracy(location.getAccuracy());
        locationJSONModel.setTransition_type(location.getTransition_type());
        return locationJSONModel;
    }
}
