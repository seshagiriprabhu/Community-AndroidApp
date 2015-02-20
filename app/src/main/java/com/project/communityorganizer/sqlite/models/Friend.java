package com.project.communityorganizer.sqlite.models;

/* Active Android libs */
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.Gson;

/* Java libraries */
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by seshagiri on 19/2/15.
 */
@Table(name="FriendList")
public class Friend extends Model{
    @Column(index = true)
    public int uid;

    @Column( index = true)
    public String display_name;

    @Column(unique = true, index = true)
    public String email;
    public String gender;
    public Date date_of_birth;
    public String phone_number;

    /* Default constructor */
    public Friend() { super(); }

    /* Storing into DB */
    public Friend(
            int uid,
            String display_name,
            String email,
            String gender,
            Date date_of_birth,
            String phone_number) {
        this.uid = uid;
        this.display_name = display_name;
        this.email = email;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.phone_number = phone_number;
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

    public static Friend findOrCreateFromJson(JSONObject json) throws JSONException {
        Friend existingFriend =
                new Select()
                        .from(Friend.class)
                        .where("uid = ?", json.getInt("uid"))
                        .executeSingle();
        if (existingFriend != null) {
            return existingFriend;
        } else {
            Friend friend = new Gson().fromJson(String.valueOf(json), Friend.class);
            friend.save();
            return friend;
        }
    }

}
