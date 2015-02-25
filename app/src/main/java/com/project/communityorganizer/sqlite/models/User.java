package com.project.communityorganizer.sqlite.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.project.communityorganizer.JSON.models.UserJSONModel;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by
 * @author seshagiri on 19/2/15.
 */
@Table(name = "User")
public class User extends Model{
    @Column(name="display_name", index = true)
    public String display_name;

    @Column(name="email", unique = true, index = true)
    public String email;

    @Column(name="password", notNull = true)
    public String password;

    @Column(name="gender")
    public String gender;

    @Column(name="date_of_birth")
    public Date date_of_birth;

    @Column(name="phone_number")
    public String phone_number;

    @Column(name="mobile_os")
    public String mobile_os;

    @Column(name="mobile_device")
    public String mobile_device;

    @Column(name = "phone_uid")
    public String phone_uid;

    @Column(name = "carrier")
    public String carrier;

    /**
     * Default constructor
     */
    public User() { super(); }

    /**
     * Constructor for the UserJSONModel object parameter
     * @param model
     * @throws ParseException
     */
    public User(UserJSONModel model)  throws ParseException {
        this.display_name = model.getDisplay_name();
        this.email = model.getEmail();
        this.password = model.getPassword();
        this.date_of_birth = model.getDate_of_birth();
        this.gender = model.getGender();
        this.phone_number = model.getPhone_number();
        this.mobile_device = model.getMobile_device();
        this.mobile_os = model.getMobile_os();
        this.phone_uid = model.getPhone_uid();
        this.carrier = model.getCarrier();
    }

    public String getDisplay_name() {
        return display_name;
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

    public String getGender() {
        return gender;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getMobile_os() {
        return mobile_os;
    }

    public String getMobile_device() {
        return mobile_device;
    }

    public String getPhone_uid() {
        return phone_uid;
    }

    public String getCarrier() {
        return carrier;
    }

    /**
     * Login suite
     * @param email
     * @param password
     * @return
     */
    public static boolean checkCredentials(String email, String password) {
        User user = new Select()
                .from(User.class)
                .where("email = ?  and password = ?", email, password)
                .executeSingle();
        if (user != null) {
            return user.getEmail().equals(email) && user.getPassword().equals(password);
        } else {
            return false;
        }
    }

    /**
     * Checks for the record in the db if not found creates a new one
     * @param model
     * @return User
     * @throws ParseException
     */
    public static User findOrCreateFromJson(UserJSONModel model) throws ParseException {
        User existingUser =
                new Select()
                        .from(User.class)
                        .where("email = ?", model.getEmail())
                        .executeSingle();
        // If a user doesn't exist in the local db
        if (existingUser == null) {
            User user= new User(model);
            user.save();
            return user;
        }
        // if the user exist check if the details are up-to-date
        if((existingUser.getEmail().equals(model.getEmail())) &&
                (existingUser.getDisplay_name().equals(model.getDisplay_name())) &&
                (existingUser.getGender().equals(model.getGender())) &&
                (existingUser.getPhone_number().equals(model.getPhone_number()))) {
            return existingUser;
        } else {
            User user = new User();
            user.display_name = model.getDisplay_name();
            user.date_of_birth = model.getDate_of_birth();
            user.phone_number = model.getPhone_number();
            user.gender = model.getGender();
            user.save();
            return user;
        }
    }

    /**
     * Returns the UserJSONModel of the current user
     * @return UserJSONModel
     */
    public static UserJSONModel getUserJSONObject(String email) {
        User user = new Select()
                .from(User.class)
                .where("email = ?", email)
                .executeSingle();
        UserJSONModel model = new UserJSONModel(user);
        return model;
    }

    public  static User getUserObject(String email) {
        return new Select()
                .from(User.class)
                .where("email = ?", email)
                .executeSingle();
    }
}
