package com.project.communityorganizer.sqlite.models;

import java.math.BigInteger;

/**
 * Created by seshagiri on 19/2/15.
 */
public class EventAttendance {
    private BigInteger aid;
    private BigInteger event_id;
    private String email;
    private String display_name;
    private String status;

    public EventAttendance(
            BigInteger aid,
            BigInteger event_id,
            String email,
            String display_name,
            String status) {
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

    public BigInteger getEvent_id() {
        return event_id;
    }

    public void setEvent_id(BigInteger event_id) {
        this.event_id = event_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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
