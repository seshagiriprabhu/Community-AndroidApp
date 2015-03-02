package com.project.communityorganizer.JSON.models;

import com.project.communityorganizer.Constants;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by
 * @author seshagiri on 1/3/15.
 */
public class EventJSONModel {
    public int event_id;
    public String event_name;
    public String event_description;
    public String event_creator;
    public String personal_feeling;
    public Date start_time;
    public Date end_time;
    public int geofence_id;

    public EventJSONModel() {
        super();
    }

    public int getEvent_id() {
        return event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_description() {
        return event_description;
    }

    public String getEvent_creator() {
        return event_creator;
    }

    public String getPersonal_feeling() {
        return personal_feeling;
    }

    public Date getStart_time() {
        return start_time;
    }

    public String getStart_time_as_String() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                Constants.DATE_TIME_FORMAT2, Locale.US);
        return dateFormatter.format(start_time);
    }

    public void setStart_time_from_epoch(String start_time_from_epoch) throws ParseException {
        this.start_time = new Date(Long.parseLong(start_time_from_epoch));
    }

    public void setStart_time2(String start_time) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(Constants.DATE_TIME_FORMAT2, Locale.US);
        try {
            this.start_time = formatter.parse(start_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setEnd_time_from_epoch(String end_time_from_epoch) throws ParseException {
        this.end_time = new Date(Long.parseLong(end_time_from_epoch));
    }

    public Date getEnd_time() {
        return end_time;
    }

    public String getEnd_time_as_String() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                Constants.DATE_TIME_FORMAT2, Locale.US);
        return dateFormatter.format(end_time);
    }

    public void setEnd_time2(String end_time) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(Constants.DATE_TIME_FORMAT2, Locale.US);
        //formatter.setTimeZone(TimeZone.getTimeZone(Constants.TIME_ZONE));
        try {
            this.end_time = formatter.parse(end_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getGeofence_id() {
        return geofence_id;
    }
}
