package com.project.communityorganizer.utils;

import android.location.Location;
import rx.functions.Func1;

/**
 * Created by
 * @author seshagiri on 22/3/15.
 */
public class LocationToStringFunc implements Func1<Location, String> {
    /**
     * {@inheritDoc}
     * @param location
     * @return
     */
    @Override
    public String call(Location location) {
        if (location != null)
            return location.getLatitude() + " " +
                    location.getLongitude() + " (" +
                    location.getAccuracy() + ")";
        return "no location available";
    }
}