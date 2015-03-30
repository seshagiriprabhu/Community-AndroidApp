package com.project.communityorganizer.utils;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import rx.functions.Func1;

/**
 * Created by
 * @author seshagiri on 22/3/15.
 */
public class ToMostProbableActivity implements Func1<ActivityRecognitionResult, DetectedActivity> {
    /**
     * {@inheritDoc}
     * @param activityRecognitionResult
     * @return
     */
    @Override
    public DetectedActivity call(ActivityRecognitionResult activityRecognitionResult) {
        return activityRecognitionResult.getMostProbableActivity();
    }
}
