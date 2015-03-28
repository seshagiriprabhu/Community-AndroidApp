package com.project.communityorganizer.sqlite.models;

/* Android core imports */
import android.database.Cursor;

/* Active Android imports */
import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/* User defined models */
import com.activeandroid.query.Select;
import com.project.communityorganizer.JSON.models.EventJSONModel;

/* Java imports */
import java.text.ParseException;
import java.util.Date;

/**
 * Created by
 * @author seshagiri on 19/2/15.
 */
@Table(name="Event")
public class Event extends Model{
    @Column(index = true, unique = true, name="event_id")
    public int event_id;

    @Column(index = true, name="event_name")
    public String event_name;

    @Column(name="event_description")
    public String event_description;

    @Column(name="Friend", index = true)
    public Friend event_creator;

    @Column(name="personal_feeling")
    public String personal_feeling;

    @Column(name="start_time")
    public Date start_time;

    @Column(name="end_time")
    public Date end_time;

    @Column(name="Geofence")
    public GeofenceModel geofence_Model_id;


    /* Default Constructor */
    public Event() { super(); }

    public int getEvent_id() {
        return event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_description() {
        return event_description;
    }

    public Friend getEvent_creator() {
        return event_creator;
    }

    public String getPersonal_feeling() {
        return personal_feeling;
    }

    public Date getStart_time() {
        return start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public GeofenceModel getGeofence_Model_id() {
        return geofence_Model_id;
    }

    public Event(EventJSONModel eventJSONModel) throws ParseException {
        this.event_id = eventJSONModel.getEvent_id();
        this.event_name = eventJSONModel.getEvent_name();
        this.event_description = eventJSONModel.getEvent_description();
        this.event_creator = Friend.getFriendDetails(eventJSONModel.getEvent_creator());
        this.personal_feeling = eventJSONModel.getPersonal_feeling();
        this.start_time = eventJSONModel.getStart_time();
        this.end_time = eventJSONModel.getEnd_time();
        int gid = eventJSONModel.getGeofence_id();
        this.geofence_Model_id = GeofenceModel.getGeofenceDetails(gid);
    }

    public static Event findOrCreateFromModel(EventJSONModel model) throws ParseException {
        Event existingEvent =
                new Select()
                        .from(Event.class)
                        .where("event_id =?", model.getEvent_id())
                        .executeSingle();
        // If the friend is not there in the local db
        if (existingEvent == null) {
            Event event = new Event(model);
            event.save();
            return event;
        }
        // If the friend already exists in local db
        if((existingEvent.getEvent_id() == model.getEvent_id()) &&
                existingEvent.getEvent_description().equals(model.getEvent_description()) &&
                existingEvent.getEvent_creator() ==
                        Friend.getFriendDetails(model.getEvent_creator()) &&
                existingEvent.getEvent_name().equals(model.getEvent_name()) &&
                existingEvent.getStart_time().equals(model.getStart_time()) &&
                existingEvent.getEnd_time().equals(model.getEnd_time()) &&
                existingEvent.getGeofence_Model_id() ==
                        GeofenceModel.getGeofenceDetails(model.getGeofence_id()) &&
                existingEvent.getPersonal_feeling().equals(model.getPersonal_feeling())) {
            return existingEvent;
        } else  {
            Event event = Event.load(Event.class, existingEvent.getId());
            event.event_id = model.getEvent_id();
            event.event_name = model.getEvent_name();
            event.event_description = model.getEvent_description();
            event.event_creator = Friend.getFriendDetails(model.getEvent_creator());
            event.personal_feeling = model.getPersonal_feeling();
            event.start_time = model.getStart_time();
            event.end_time = model.getEnd_time();
            int gid = model.getGeofence_id();
            event.geofence_Model_id = GeofenceModel.getGeofenceDetails(gid);
            return event;
        }
    }

    public static Event getEventDetails(int event_id) {
        return new Select()
                .from(Event.class)
                .where("event_id = ?", event_id)
                .executeSingle();
    }

    public static Cursor fetchResultCursor() {
        String tableName = Cache.getTableInfo(Event.class).getTableName();
        String resultRecords = new Select(tableName + ".*, " + tableName + ".Id as _id")
                .from(Event.class)
                .toSql();
        return Cache.openDatabase().rawQuery(resultRecords, null);
    }
}
