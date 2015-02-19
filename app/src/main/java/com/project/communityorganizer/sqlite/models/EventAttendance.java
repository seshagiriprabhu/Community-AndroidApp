package com.project.communityorganizer.sqlite.models;

/* Active Android libs */
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/* User defined models */
import com.project.communityorganizer.sqlite.models.Event;
import com.project.communityorganizer.sqlite.models.Friend;

/* Java libs */
import java.math.BigInteger;

/**
 * Created by seshagiri on 19/2/15.
 */
@Table(name="EventAttendance")
public class EventAttendance extends Model{
    @Column(unique = true)
    public BigInteger aid;

    @Column(name="Event")
    public Event event_id;

    @Column(name="Friend")
    public Friend email;
    public String display_name;
    public String status;

    /* Default Constructor */
    public EventAttendance() { super(); }

    /* Detailed Constructor */

    public EventAttendance(
            BigInteger aid,
            Event event_id,
            Friend email,
            String display_name,
            String status) {
        super();
        this.aid = aid;
        this.event_id = event_id;
        this.email = email;
        this.display_name = display_name;
        this.status = status;
    }

    public BigInteger getAid() {
        return aid;
    }

    public void setAid(BigInteger aid) {
        this.aid = aid;
    }

    public Event getEvent_id() {
        return event_id;
    }

    public void setEvent_id(Event event_id) {
        this.event_id = event_id;
    }

    public Friend getEmail() {
        return email;
    }

    public void setEmail(Friend email) {
        this.email = email;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
