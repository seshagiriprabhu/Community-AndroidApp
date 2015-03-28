package com.project.communityorganizer.JSON.models;

/**
 * Created by
 * @author seshagiri on 22/2/15.
 */
public class GeofenceJSONModel {
    public int gid;
    public String fence_name;
    public Double latitude;
    public Double longitude;
    public float geofence_radius;
    public int expiration_time;

    public GeofenceJSONModel() {
        super();
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
}
