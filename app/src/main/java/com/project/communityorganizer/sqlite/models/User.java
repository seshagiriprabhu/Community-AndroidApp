package com.project.communityorganizer.sqlite.models;

/* Active Android libs */
import android.text.method.DateTimeKeyListener;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/* Java libs */
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by seshagiri on 19/2/15.
 */
@Table(name = "User")
public class User extends Model{
    public int uid;

    @Column(index = true)
    public String display_name;

    @Column(unique = true, index = true)
    public String email;

    @Column(notNull = true)
    public String password;
    public String gender;
    public Date date_of_birth;
    public String phone_number;
    public String mobile_os;
    public String mobile_device;
    public String phone_uid;
    public String carrier;

    /* Default constructor */
    public User() { super(); }

    /* Constructor for JSON data */
    public User(String display_name,
                String email,
                String password,
                String gender,
                Date date_of_birth,
                String phone_number,
                String mobile_os,
                String mobile_device,
                String phone_uid,
                String carrier) {
        super();
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
    public User(int uid,
                String display_name,
                String email,
                String password,
                String gender,
                Date date_of_birth,
                String phone_number,
                String mobile_os,
                String mobile_device,
                String phone_uid,
                String carrier) {
        super();
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

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
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

    public static User getLoginDetails(String email, String password) {
        return new Select()
                .from(User.class)
                .where("email = ? and password = ?", email, password)
                .executeSingle();
    }
}
