package com.project.communityorganizer.JSON.models;

/**
 * Created by seshagiri on 22/2/15.
 */
public class GeofenceJSONModel {
    public int gid;
    public String fence_name;
    public Double latitude;
    public Double longitude;
    public Double geofence_radius;
    public int expiration_time;

    public GeofenceJSONModel() {
        super();
    }

    public GeofenceJSONModel(
            int gid,
            String fence_name,
            Double latitude,
            Double longitude,
            Double geofence_radius,
            int expiration_time)
    {
        super();
        this.gid = gid;
        this.fence_name = fence_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geofence_radius = geofence_radius;
        this.expiration_time = expiration_time;
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
}
