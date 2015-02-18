package com.project.communityorganizer;

/**
 * Created by seshagiri on 18/2/15.
 */
public class User {
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

    public String getPhone_number() {
        return phone_number;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    public String getDate_of_birth() {
        return date_of_birth;
    }
    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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
    public String getMobile_os() { return mobile_os; }
    public void setMobile_os(String os){ this.mobile_os = os; }
    public String getMobile_device() { return mobile_device; }
    public void setMobile_device(String device){ this.mobile_device = device; }
    public String getPhone_uid() { return phone_uid; }
    public void setPhone_uid(String uid) { this.phone_uid = uid; }
    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }
}
