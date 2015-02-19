package com.project.communityorganizer.sqlite.models;

/**
 * Created by seshagiri on 19/2/15.
 */
public class Friend {
    private String display_name;
    private String email;
    private String gender;
    private String date_of_birth;
    private String phone_number;

    /* Default constructor */
    public Friend() {}

    /* Constructor for storing into DB */
    public Friend(String display_name,
                  String email,
                  String gender,
                  String date_of_birth,
                  String phone_number) {
        this.display_name = display_name;
        this.email = email;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.phone_number = phone_number;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
