package com.project.communityorganizer.sqlite.models;

/* Active Android libs */
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/* User defined models */
import com.activeandroid.query.Select;
import com.project.communityorganizer.JSON.models.EventAttendanceJSONModel;

import java.util.List;

/**
 * Created by
 * @author seshagiri on 19/2/15.
 */
@Table(name="EventAttendance")
public class EventAttendance extends Model{
    @Column(unique = true, name="aid")
    public int aid;

    @Column(name="Event")
    public Event event_id;

    @Column(name="Friend")
    public Friend email;

    @Column(name="status")
    public String status;

    /* Default Constructor */
    public EventAttendance() { super(); }

    /**
     *  Detailed Constructor
     */
    public EventAttendance(
            int aid,
            int event_id,
            String email,
            String status) {
        super();
        this.aid = aid;
        Event event = Event.getEventDetails(event_id);
        this.event_id = event;
        Friend friend = Friend.getFriendDetails(email);
        this.email = friend;
        this.status = status;
    }

    public int getAid() {
        return aid;
    }

    public Event getEvent_id() {
        return event_id;
    }

    public Friend getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Constructor using EventAttendanceJSONModel object
     * @param json
     */
    public EventAttendance(EventAttendanceJSONModel json) {
        Event event = Event.getEventDetails(json.getEvent_id());
        Friend friend = Friend.getFriendDetails(json.getEmail());
        this.aid = json.getAid();
        this.event_id = event;
        this.email = friend;
        this.status = json.getStatus();
    }

    /**
     * Returns the event attendance details of
     * @param aid
     * @return
     */
    public static EventAttendance getEventAttendanceDetails(int aid) {
        return new Select()
                .from(EventAttendance.class)
                .where("aid = ?", aid)
                .executeSingle();
    }

    /**
     * Returns the list of attendees
     * @return
     */
    public static List<EventAttendance> getEventAttendeesList(Event event) {
        return new Select()
                .from(EventAttendance.class)
                .where("event = ?", event.getId())
                .execute();
    }

    /**
     * Finds or creates EventAttendance model from the EventAttendanceJSONModel
     * @param model
     * @return
     */
    public static EventAttendance findOrCreateFromModel(EventAttendanceJSONModel model) {
        EventAttendance  exitingEventAttendance = new Select()
                .from(EventAttendance.class)
                .where("aid = ?", model.getAid())
                .executeSingle();
        if (exitingEventAttendance == null) {
            EventAttendance eventAttendance = new EventAttendance(model);
            eventAttendance.save();
            return eventAttendance;
        }
        if (exitingEventAttendance.getAid() == model.getAid() &&
                exitingEventAttendance.getStatus().equals(model.getStatus())) {
            return exitingEventAttendance;
        } else {
            EventAttendance attendance = EventAttendance.load(EventAttendance.class,
                    exitingEventAttendance.getId());
            attendance.email = Friend.getFriendDetails(model.getEmail());
            attendance.event_id = Event.getEventDetails(model.getEvent_id());
            attendance.status = model.getStatus();
            return attendance;
        }
    }
}