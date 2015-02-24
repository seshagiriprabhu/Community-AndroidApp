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
import java.util.Date;

/**
 * Created by
 * @author Seshagiri on 19/2/15.
 */
@Table(name = "PhoneData")
public class PhoneData extends Model{
    @Column(name="User")
    public User email;
    public Date date_time;
    public String battery_state;
    public Double app_power_consumption;
    public Double avg_mem_util;
    public Double avg_cpu_util;
    public String last_online_time;
    public Double last_online_duration;
    public String connection_method;
    public Double app_data_transfered;

    /**
     * Default Constructor
     */
    public PhoneData() { super(); }

    /**
     *  Constructor for storing into DB
     */
    public PhoneData(
            String email,
            Date date_time,
            String battery_state,
            Double app_power_consumption,
            Double avg_mem_util,
            Double avg_cpu_util,
            String last_online_time,
            Double last_online_duration,
            String connection_method,
            Double app_data_transfered) {
        super();
        User user = User.getUserObject(email);
        this.email = user;
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
}
