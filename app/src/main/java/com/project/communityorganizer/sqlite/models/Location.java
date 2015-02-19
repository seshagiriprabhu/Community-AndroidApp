package com.project.communityorganizer.sqlite.models;

import java.math.BigDecimal;

/**
 * Created by seshagiri on 19/2/15.
 */
public class Location {
    private String email;
    private String date_time;
    private BigDecimal accuracy;
    private Integer transition_type;
    private BigDecimal gid;

    /* Default constructor */
    public Location() {}

    /* Storing into local DB */
    public Location(
            String email,
            String date_time,
            BigDecimal accuracy,
            Integer transition_type,
            BigDecimal gid) {
        this.email = email;
        this.date_time = date_time;
        this.accuracy = accuracy;
        this.transition_type = transition_type;
        this.gid = gid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public BigDecimal getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(BigDecimal accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getTransition_type() {
        return transition_type;
    }

    public void setTransition_type(Integer transition_type) {
        this.transition_type = transition_type;
    }

    public BigDecimal getGid() {
        return gid;
    }

    public void setGid(BigDecimal gid) {
        this.gid = gid;
    }
}
