package com.project.communityorganizer.sqlite.models;

import android.provider.ContactsContract;
import android.text.format.Time;

/* Active Android libs */
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.project.communityorganizer.JSON.models.GeofenceJSONModel;


/* Java libraries */
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;

/**
 * Created by seshagiri on 19/2/15.
 */
@Table(name="Geofence")
public class Geofence extends Model {
    @Column(index = true)
    public int gid;
    @Column(index = true)
    public String fence_name;
    public Double latitude;
    public Double longitude;
    public Double geofence_radius;
    public int expiration_time;

    /* Default constructor */
    public Geofence() { super(); }

    /* Constructor for storing in DB */

    public Geofence(
            int gid,
            String fence_name,
            Double latitude,
            Double longitude,
            Double geofence_radius,
            int expiration_time) {
        super();
        this.gid = gid;
        this.fence_name = fence_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geofence_radius = geofence_radius;
        this.expiration_time = expiration_time;
    }

    public Geofence(GeofenceJSONModel model) {
        this.gid = model.getGid();
        this.fence_name = model.getFence_name();
        this.longitude = model.getLongitude();
        this.latitude = model.getLatitude();
        this.geofence_radius = model.getGeofence_radius();
        this.expiration_time = model.getExpiration_time();
    }

    public static Geofence getGeofenceDetails(int gid) {
        return new Select().from(Geofence.class).where("gid = ?", gid).executeSingle();
    }

    public static Geofence findOrCreateFromModel(GeofenceJSONModel model) {
        Geofence existingGeofence = new Select().from(Geofence.class)
                .where("gid = ?", model.getGid()).executeSingle();
        if (existingGeofence != null ) {
            return existingGeofence;
        } else {
            Geofence geofence = new Geofence(model);
            geofence.save();
            return geofence;
        }

    }
}

