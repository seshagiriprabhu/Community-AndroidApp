package com.project.communityorganizer.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.project.communityorganizer.JSON.models.EventJSONModel;
import com.project.communityorganizer.R;
import com.project.communityorganizer.Services.SaveSharedPreference;
import com.project.communityorganizer.sqlite.models.Friend;

import java.text.ParseException;

/**
 * Created by
 * @author seshagiri on 1/3/15.
 */
public class EventListCursorAdaptor extends CursorAdapter  {
    EventJSONModel eventJSONModel;

    /**
     * Constructor
     * @param context
     * @param c
     */
    public EventListCursorAdaptor(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     * {@inheritDoc}
     * @param context
     * @param cursor
     * @param parent
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.event_list, parent, false);
    }

    /**
     * {@inheritDoc}
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = new ViewHolder();
        eventJSONModel = new EventJSONModel();
        eventJSONModel.event_name = cursor.getString(cursor.getColumnIndexOrThrow("event_name"));
        eventJSONModel.event_creator = cursor.getString(cursor.getColumnIndexOrThrow("Friend"));
        try {
            eventJSONModel.setStart_time_from_epoch(
                    cursor.getString(cursor.getColumnIndexOrThrow("start_time")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.eventName = (TextView) view.findViewById(R.id.eventName);
        viewHolder.eventCreator = (TextView) view.findViewById(R.id.creator_name);
        viewHolder.startTime = (TextView) view.findViewById(R.id.start_time);

        Friend friend = Friend.getFriendIdDetails(Long.valueOf(eventJSONModel.getEvent_creator()));

        if (!SaveSharedPreference.getUserEmail(context).equals(friend.getEmail()))
            viewHolder.eventCreator.setText(friend.getDisplay_name());
        else viewHolder.eventCreator.setText(friend.getDisplay_name() + " (you)");

        viewHolder.eventName.setText(eventJSONModel.getEvent_name());
        viewHolder.startTime.setText(eventJSONModel.getStart_time_as_String());

        view.setTag(viewHolder);
    }

    /**
     * A class to hold the items of a list
     */
    private class ViewHolder {
        public TextView eventName;
        public TextView eventCreator;
        public TextView startTime;
    }
}
