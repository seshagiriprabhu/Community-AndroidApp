package com.project.communityorganizer.sqlite.models;

import android.provider.ContactsContract;
import android.text.format.Time;

/* Active Android libs */
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


/* Java libraries */
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by seshagiri on 19/2/15.
 */
@Table(name="Geofence")
public class Geofence extends Model {
    @Column(unique = true, notNull = true, index = true)
    public BigInteger gid;
    @Column(index = true)
    public String fence_name;
    public BigDecimal latitude;
    public BigDecimal longitude;
    public BigDecimal geofence_radius;
    public Integer expiration_time;

    /* Default constructor */
    public Geofence() { super(); }

    /* Constructor for storing in DB */

    public Geofence(
            BigInteger gid,
            String fence_name,
            BigDecimal latitude,
            BigDecimal longitude,
            BigDecimal geofence_radius,
            Integer expiration_time) {
        super();
        this.gid = gid;
        this.fence_name = fence_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geofence_radius = geofence_radius;
        this.expiration_time = expiration_time;
    }

    public BigInteger getGid() {
        return gid;
    }

    public void setGid(BigInteger gid) {
        this.gid = gid;
    }

    public String getFence_name() {
        return fence_name;
    }

    public void setFence_name(String fence_name) {
        this.fence_name = fence_name;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getGeofence_radius() {
        return geofence_radius;
    }

    public void setGeofence_radius(BigDecimal geofence_radius) {
        this.geofence_radius = geofence_radius;
    }

    public Integer getExpiration_time() {
        return expiration_time;
    }

    public void setExpiration_time(Integer expiration_time) {
        this.expiration_time = expiration_time;
    }
}

