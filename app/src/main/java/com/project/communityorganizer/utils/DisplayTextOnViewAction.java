package com.project.communityorganizer.utils;

import android.widget.TextView;

import rx.functions.Action1;

/**
 * Created by
 * @author seshagiri on 29/3/15.
 */
public class DisplayTextOnViewAction implements Action1<String> {
    private final TextView target;

    public DisplayTextOnViewAction(TextView target) {
        this.target = target;
    }

    @Override
    public void call(String s) {
        target.setText(s);
    }
}
