package com.project.communityorganizer.sqlite.models;

/* Active Android libs */
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.project.communityorganizer.JSON.models.GeofenceJSONModel;

import java.util.List;

/**
 * Created by
 * @author seshagiri on 19/2/15.
 */
@Table(name="Geofence")
public class GeofenceModel extends Model {
    @Column(name="gid",  index = true, unique= true)
    public int gid;

    @Column(name="fence_name", index = true)
    public String fence_name;

    @Column(name="latitude")
    public Double latitude;

    @Column(name="longitude")
    public Double longitude;

    @Column(name="geofence_radius")
    public float geofence_radius;

    @Column(name = "expiration_time")
    public int expiration_time;

    /* Default constructor */
    public GeofenceModel() { super(); }

    /**
     * Storing in DB using JSON model
     * @param model
     */
    public GeofenceModel(GeofenceJSONModel model) {
        this.gid = model.getGid();
        this.fence_name = model.getFence_name();
        this.longitude = model.getLongitude();
        this.latitude = model.getLatitude();
        this.geofence_radius = model.getGeofence_radius();
        this.expiration_time = model.getExpiration_time();
    }

    public int getGid() {
        return gid;
    }

    public String getFence_name() {
        return fence_name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public float getGeofence_radius() {
        return geofence_radius;
    }

    public int getExpiration_time() {
        return expiration_time;
    }

    public static GeofenceModel getGeofenceDetails(int gid) {
        return new Select()
                .from(GeofenceModel.class)
                .where("gid = ?", gid)
                .executeSingle();
    }

    /**
     * A function to store into DB using a JSON model
     * @param model
     * @return
     */
    public static GeofenceModel findOrCreateFromModel(GeofenceJSONModel model) {
        GeofenceModel existingGeofenceModel = new Select()
                .from(GeofenceModel.class)
                .where("gid = ?", model.getGid())
                .executeSingle();
        if (existingGeofenceModel == null) {
            GeofenceModel geofenceModel = new GeofenceModel(model);
            geofenceModel.save();
            return geofenceModel;
        }

        if (existingGeofenceModel.getGid() == model.getGid() &&
                existingGeofenceModel.getFence_name().equals(model.getFence_name()) &&
                existingGeofenceModel.getGeofence_radius() == model.getGeofence_radius() &&
                existingGeofenceModel.getExpiration_time() == model.getExpiration_time() &&
                existingGeofenceModel.getLatitude().equals(model.getLatitude()) &&
                existingGeofenceModel.getLongitude().equals(model.getLongitude())) {
            return existingGeofenceModel;
        } else {
            GeofenceModel geofenceModel = GeofenceModel.load(GeofenceModel.class, existingGeofenceModel.getId());
            geofenceModel.geofence_radius = model.getGeofence_radius();
            geofenceModel.fence_name = model.getFence_name();
            geofenceModel.longitude = model.getLongitude();
            geofenceModel.latitude = model.getLatitude();
            geofenceModel.expiration_time = model.getExpiration_time();
            geofenceModel.save();
            return geofenceModel;
        }
    }

    /**
     * Returns the list of all the geofences in the local DB
     * @return
     */
    public static List<GeofenceModel> getAllGeofenceDetails() {
        return new Select().from(GeofenceModel.class).execute();
    }

    /**
     * Returns a geofence object of the given parameters
     */
    public static GeofenceModel getGeofenceModelLatLong(double latitude, double longitude) {
        return new Select()
                .from(GeofenceModel.class)
                .where("latitude = ?", latitude, "longitude = ?", longitude)
                .executeSingle();
    }
}

