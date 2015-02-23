package com.project.communityorganizer.sqlite.models;

/* Active Android libs */
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.project.communityorganizer.JSON.models.FriendJSONModel;

/* Java libraries */
import java.text.ParseException;
import java.util.Date;

/**
 * Created by
 * @author seshagiri on 19/2/15.
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


    public Friend(FriendJSONModel model)  throws ParseException {
        this.display_name = model.getDisplay_name();
        this.email = model.getEmail();
        this.date_of_birth = model.date_of_birth;
        this.gender = model.getGender();
        this.phone_number = model.getPhone_number();
    }


    public static Friend findOrCreateFromModel(FriendJSONModel model) throws ParseException {
        Friend existingFriend =
                new Select()
                        .from(Friend.class)
                        .where("email = ?", model.getEmail())
                        .executeSingle();
        if (existingFriend != null) {
            return existingFriend;
        } else {
            Friend friend = new Friend(model);
            friend.save();
            return friend;

        }
    }

    public String getEmail() {
        return email;
    }

    public static Friend getFriendDetails(String email) {
        return new Select().from(Friend.class).where("email = ?", email).executeSingle();
    }
}
