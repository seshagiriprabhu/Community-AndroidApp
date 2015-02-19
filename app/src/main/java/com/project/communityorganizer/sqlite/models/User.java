package com.project.communityorganizer.sqlite.models;

import java.math.BigInteger;

/**
 * Created by seshagiri on 19/2/15.
 */
public class User {
    private BigInteger uid;
    private String display_name;
    private String email;
    private String password;
    private String gender;
    private String date_of_birth;
    private String phone_number;
    private String mobile_os;
    private String mobile_device;
    private String phone_uid;
    private String carrier;

    /* Default constructor */
    public User() {}

    /* Constructor for JSON data */
    public User(String display_name,
                String email,
                String password,
                String gender,
                String date_of_birth,
                String phone_number,
                String mobile_os,
                String mobile_device,
                String phone_uid,
                String carrier) {
        this.display_name = display_name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.phone_number = phone_number;
        this.mobile_os = mobile_os;
        this.mobile_device = mobile_device;
        this.phone_uid = phone_uid;
        this.carrier = carrier;
    }

    /* Constructor for storing DB */
    public User(BigInteger uid,
                String display_name,
                String email,
                String password,
                String gender,
                String date_of_birth,
                String phone_number,
                String mobile_os,
                String mobile_device,
                String phone_uid,
                String carrier) {
        this.uid = uid;
        this.display_name = display_name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.phone_number = phone_number;
        this.mobile_os = mobile_os;
        this.mobile_device = mobile_device;
        this.phone_uid = phone_uid;
        this.carrier = carrier;
    }

    public BigInteger getUid() {
        return uid;
    }

    public void setUid(BigInteger uid) {
        this.uid = uid;
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
}
