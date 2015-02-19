package com.project.communityorganizer.sqlite.models;

import android.provider.ContactsContract;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by seshagiri on 19/2/15.
 */
public class Geofence {
    private BigInteger gid;
    private String fence_name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal geofence_radius;
    private Integer expiration_time;
    private String date_time;

    /* Default constructor */
    public Geofence() {}

    /* Constructor for storing into DB */
    public Geofence(
            BigInteger gid,
            String fence_name,
            BigDecimal latitude,
            BigDecimal longitude,
            BigDecimal geofence_radius,
            Integer expiration_time,
            String date_time) {
        this.gid = gid;
        this.fence_name = fence_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geofence_radius = geofence_radius;
        this.expiration_time = expiration_time;
        this.date_time = date_time;
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

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

}

