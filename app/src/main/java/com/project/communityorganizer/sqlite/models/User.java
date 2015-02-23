package com.project.communityorganizer.sqlite.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.project.communityorganizer.JSON.models.UserJSONModel;

/* Java libs */
import java.text.ParseException;
import java.util.Date;

/**
 * Created by
 * @author seshagiri on 19/2/15.
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

    public User(UserJSONModel model)  throws ParseException {
        this.display_name = model.getDisplay_name();
        this.email = model.getEmail();
        this.password = model.getPassword();
        this.date_of_birth = model.date_of_birth;
        this.gender = model.getGender();
        this.phone_number = model.getPhone_number();
        this.mobile_device = model.getMobile_device();
        this.mobile_os = model.getMobile_os();
        this.phone_uid = model.getPhone_uid();
        this.carrier = model.getCarrier();
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

    public static User findOrCreateFromJson(UserJSONModel model) throws ParseException {
        User existingFriend =
                new Select()
                        .from(User.class)
                        .where("email = ?", model.getEmail())
                        .executeSingle();
        if (existingFriend != null) {
            return existingFriend;
        } else {
            User user= new User(model);
            user.save();
            return user;
        }
    }


    public static String getRegisteredUserEmail() {
        return String.valueOf(new Select("email")
                .from(User.class)
                .execute());
    }

    public static String getRegisteredUserDisplayName(){
        return String.valueOf(new Select("display_name")
        .from(User.class)
        .execute());
    }

    public static User getUserDetails(String email) {
        return new Select().from(User.class).where("email = ?", email).executeSingle();

    }
}
