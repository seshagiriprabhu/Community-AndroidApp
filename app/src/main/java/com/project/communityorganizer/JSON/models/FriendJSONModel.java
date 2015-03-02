package com.project.communityorganizer.JSON.models;
import android.os.Parcel;
import android.os.Parcelable;

import com.project.communityorganizer.Constants;
import com.project.communityorganizer.sqlite.models.Friend;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by
 * @author seshagiri on 22/2/15.
 */
public class FriendJSONModel {
    public String display_name;
    public String email;
    public String gender;
    public Date date_of_birth;
    public String phone_number;

    /**
     * Default Constructor
     */
    public FriendJSONModel() {
        super();
    }

    /**
     * Friend Constructor with params
     * @param display_name
     * @param email
     * @param gender
     * @param date_of_birth
     * @param phone_number
     * @throws ParseException
     */
    public FriendJSONModel(
            String display_name,
            String email,
            String gender,
            Date date_of_birth,
            String phone_number) throws ParseException {
        super();
        this.display_name = display_name;
        this.email = email;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.phone_number = phone_number;
    }

    /**
     * Constructor with Friend object
     * @param friend
     */
    public FriendJSONModel(Friend friend){
        super();
        this.display_name = friend.getDisplay_name();
        this.email = friend.getEmail();
        this.date_of_birth = friend.getDate_of_birth();
        this.gender = friend.getGender();
        this.phone_number = friend.getPhone_number();
    }


    public String getDisplay_name() {
        return display_name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void set_Date_of_birth_from_epoch(String date_of_birth) throws ParseException {
       this.date_of_birth = new Date(Long.parseLong(date_of_birth));
    }

    public void setDate_of_birth_from_utc(String date_of_birth_from_utc) throws ParseException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(Constants.TIME_ZONE));
        this.date_of_birth = dateFormatter.parse(date_of_birth_from_utc);
    }


    public String getDate_of_birth_as_string() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(Constants.TIME_ZONE));
        return dateFormatter.format(date_of_birth);
    }
}
