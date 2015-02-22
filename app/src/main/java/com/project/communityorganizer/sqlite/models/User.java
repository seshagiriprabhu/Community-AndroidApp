package com.project.communityorganizer.sqlite.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.Gson;

/* Java libs */
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by seshagiri on 19/2/15.
 */
@Table(name = "User")
public class User extends Model{
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

    /* Constructor for storing DB */
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


    public User(JSONObject json) throws JSONException, ParseException {
        this.display_name = json.getString("display_name");
        this.email = json.getString("email");
        this.password = json.getString("password");
        String DOB = json.getString("date_of_birth");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = format.parse(DOB);
        this.date_of_birth = date;
        this.phone_number = json.getString("phone_number");
        this.mobile_os = json.getString("mobile_os");
        this.mobile_device = json.getString("mobile_device");
        this.carrier = json.getString("carrier");
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


    public static boolean checkCredentials(String email, String password) {
        User user = new Select(email, password)
                .from(User.class)
                .where("email = ?  and password = ?", email, password)
                .executeSingle();
        return user.getEmail().equals(email) && user.getPassword().equals(password);
    }

    public static User findOrCreateFromJson(JSONObject json) throws JSONException, ParseException {
        User existingFriend =
                new Select()
                        .from(User.class)
                        .where("email = ?", json.getString("email"))
                        .executeSingle();
        if (existingFriend != null) {
            return existingFriend;
        } else {
            User user= new User(json);
            user.save();
            return user;
        }
    }

    public static User registerNewUser(JSONObject json) throws JSONException, ParseException {
        User user= new User(json);
        user.save();
        return user;
    }

    public static String getAll(String email) {
        return String.valueOf(new Select(email)
                .from(User.class)
                .where("email = ?", email)
                .executeSingle());

    }

    public static User getUserDetails(String email) {
        return new Select().from(User.class).where("email = ?", email).executeSingle();

    }
}
