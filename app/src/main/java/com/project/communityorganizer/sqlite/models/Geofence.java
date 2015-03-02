package com.project.communityorganizer.sqlite.models;

/* Active Android libs */
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.project.communityorganizer.Constants;
import com.project.communityorganizer.JSON.models.GeofenceJSONModel;

/**
 * Created by
 * @author seshagiri on 19/2/15.
 */
@Table(name="Geofence")
public class Geofence extends Model {
    @Column(name="gid",  index = true)
    public int gid;

    @Column(name="fence_name", index = true)
    public String fence_name;

    @Column(name="latitude")
    public Double latitude;

    @Column(name="longitude")
    public Double longitude;

    @Column(name="geofence_radius")
    public Double geofence_radius;

    @Column(name = "expiration_time")
    public int expiration_time;

    /* Default constructor */
    public Geofence() { super(); }

    public Geofence(GeofenceJSONModel model) {
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

    public Double getGeofence_radius() {
        return geofence_radius;
    }

    public int getExpiration_time() {
        return expiration_time;
    }

    public static Geofence getGeofenceDetails(int gid) {
        return new Select()
                .from(Geofence.class)
                .where("gid = ?", gid)
                .executeSingle();
    }

    public static Geofence findOrCreateFromModel(GeofenceJSONModel model) {
        Geofence existingGeofence = new Select()
                .from(Geofence.class)
                .where("gid = ?", model.getGid())
                .executeSingle();
        if (existingGeofence == null) {
            Geofence geofence = new Geofence(model);
            geofence.save();
            return geofence;
        }

        if (existingGeofence.getGid() == model.getGid() &&
                existingGeofence.getFence_name().equals(model.getFence_name()) &&
                existingGeofence.getGeofence_radius().equals(model.getGeofence_radius()) &&
                existingGeofence.getExpiration_time() == model.getExpiration_time() &&
                existingGeofence.getLatitude().equals(model.getLatitude()) &&
                existingGeofence.getLongitude().equals(model.getLongitude())) {
            return existingGeofence;
        } else {
            Geofence geofence = Geofence.load(Geofence.class, existingGeofence.getId());
            geofence.geofence_radius = model.getGeofence_radius();
            geofence.fence_name = model.getFence_name();
            geofence.longitude = model.getLongitude();
            geofence.latitude = model.getLatitude();
            geofence.expiration_time = model.getExpiration_time();
            geofence.save();
            return geofence;
        }
    }
}

