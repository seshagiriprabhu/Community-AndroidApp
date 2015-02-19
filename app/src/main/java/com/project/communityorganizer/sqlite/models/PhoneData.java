package com.project.communityorganizer.sqlite.models;

/* Android core libs */
import android.text.format.Time;

/* Active Android libs */
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/* User defined classes */
import com.project.communityorganizer.sqlite.models.User;

/* Java libraries */
import java.math.BigDecimal;

/**
 * Created by seshagiri on 19/2/15.
 */
@Table(name = "PhoneData")
public class PhoneData extends Model{
    @Column(name="User")
    public User email;
    public Time date_time;
    public String battery_state;
    public BigDecimal app_power_consumption;
    public BigDecimal avg_mem_util;
    public BigDecimal avg_cpu_util;
    public String last_online_time;
    public BigDecimal last_online_duration;
    public String connection_method;
    public BigDecimal app_data_transfered;

    public PhoneData() { super(); }

    public PhoneData(
            User email,
            Time date_time,
            String battery_state,
            BigDecimal app_power_consumption,
            BigDecimal avg_mem_util,
            BigDecimal avg_cpu_util,
            String last_online_time,
            BigDecimal last_online_duration,
            String connection_method,
            BigDecimal app_data_transfered) {
        super();
        this.email = email;
        this.date_time = date_time;
        this.battery_state = battery_state;
        this.app_power_consumption = app_power_consumption;
        this.avg_mem_util = avg_mem_util;
        this.avg_cpu_util = avg_cpu_util;
        this.last_online_time = last_online_time;
        this.last_online_duration = last_online_duration;
        this.connection_method = connection_method;
        this.app_data_transfered = app_data_transfered;
    }

    public User getEmail() {
        return email;
    }

    public void setEmail(User email) {
        this.email = email;
    }

    public Time getDate_time() {
        return date_time;
    }

    public void setDate_time(Time date_time) {
        this.date_time = date_time;
    }

    public String getBattery_state() {
        return battery_state;
    }

    public void setBattery_state(String battery_state) {
        this.battery_state = battery_state;
    }

    public BigDecimal getApp_power_consumption() {
        return app_power_consumption;
    }

    public void setApp_power_consumption(BigDecimal app_power_consumption) {
        this.app_power_consumption = app_power_consumption;
    }

    public BigDecimal getAvg_mem_util() {
        return avg_mem_util;
    }

    public void setAvg_mem_util(BigDecimal avg_mem_util) {
        this.avg_mem_util = avg_mem_util;
    }

    public BigDecimal getAvg_cpu_util() {
        return avg_cpu_util;
    }

    public void setAvg_cpu_util(BigDecimal avg_cpu_util) {
        this.avg_cpu_util = avg_cpu_util;
    }

    public String getLast_online_time() {
        return last_online_time;
    }

    public void setLast_online_time(String last_online_time) {
        this.last_online_time = last_online_time;
    }

    public BigDecimal getLast_online_duration() {
        return last_online_duration;
    }

    public void setLast_online_duration(BigDecimal last_online_duration) {
        this.last_online_duration = last_online_duration;
    }

    public String getConnection_method() {
        return connection_method;
    }

    public void setConnection_method(String connection_method) {
        this.connection_method = connection_method;
    }

    public BigDecimal getApp_data_transfered() {
        return app_data_transfered;
    }

    public void setApp_data_transfered(BigDecimal app_data_transfered) {
        this.app_data_transfered = app_data_transfered;
    }
}
