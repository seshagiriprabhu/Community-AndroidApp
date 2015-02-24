package com.project.communityorganizer.JSON.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public FriendJSONModel() {
        super();
    }

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
}
