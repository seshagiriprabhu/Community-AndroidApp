package com.project.communityorganizer.sqlite.models;

/* Active Android libs */
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/* Java libraries */
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by seshagiri on 19/2/15.
 */
@Table(name="FriendList")
public class Friend extends Model{
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
            String display_name,
            String email,
            String gender,
            Date date_of_birth,
            String phone_number) {
        this.display_name = display_name;
        this.email = email;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.phone_number = phone_number;
    }

    public Friend(JSONObject json) throws JSONException, ParseException {
        this.display_name = json.getString("display_name");
        this.email = json.getString("email");
        String DOB = json.getString("date_of_birth");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = format.parse(DOB);
        this.date_of_birth = date;
        this.phone_number = json.getString("phone_number");
    }

    public String getEmail() {
        return email;
    }

    public static Friend findOrCreateFromJson(JSONObject json) throws JSONException, ParseException {
        Friend existingFriend =
                new Select()
                        .from(Friend.class)
                        .where("email = ?", json.getString("email"))
                        .executeSingle();
        if (existingFriend != null) {
            return existingFriend;
        } else {
            Friend friend = new Friend(json);
            friend.save();
            return friend;
        }
    }

    public static Friend getFriendDetails(String email) {
        return new Select().from(Friend.class).where("email = ?", email).executeSingle();
    }
}
