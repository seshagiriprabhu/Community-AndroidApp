package com.project.communityorganizer.JSON.models;
import com.project.communityorganizer.Constants;
import com.project.communityorganizer.sqlite.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by
 * @author seshagiri on 22/2/15.
 */
public class UserJSONModel {
    public String display_name;
    public String email;
    public String password;
    public String gender;
    public Date date_of_birth;
    public String phone_number;
    public String mobile_os;
    public String mobile_device;
    public String phone_uid;
    public String carrier;

    /**
     * Default constructor
     */
    public UserJSONModel() {
        super();
    }

    /**
     * Constructor for creating model from User model
     * @param user
     */
    public UserJSONModel(User user) {
        super();
        this.display_name = user.getDisplay_name();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.gender = user.getGender();
        this.date_of_birth = user.getDate_of_birth();
        this.phone_number = user.getPhone_number();
        this.mobile_device = user.getMobile_device();
        this.mobile_os = user.getMobile_os();
        this.phone_uid = user.getPhone_uid();
        this.carrier = user.getCarrier();
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getMobile_os() {
        return mobile_os;
    }

    public void setMobile_os(String mobile_os) {
        this.mobile_os = mobile_os;
    }

    public String getMobile_device() {
        return mobile_device;
    }

    public void setMobile_device(String mobile_device) {
        this.mobile_device = mobile_device;
    }

    public String getPhone_uid() {
        return phone_uid;
    }

    public void setPhone_uid(String phone_uid) {
        this.phone_uid = phone_uid;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public void set_Date_of_birth_from_epoch(String date_of_birth) throws ParseException {
        this.date_of_birth = new Date(Long.parseLong(date_of_birth));
    }

    public void setDate_of_birth_from_utc(String date_of_birth_from_utc) throws ParseException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
        this.date_of_birth = dateFormatter.parse(date_of_birth_from_utc);
    }
}
