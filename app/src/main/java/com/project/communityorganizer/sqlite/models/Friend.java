package com.project.communityorganizer.sqlite.models;

/* Active Android libs */
import android.database.Cursor;
import android.database.SQLException;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.project.communityorganizer.Constants;
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
    @Column(name="display_name", index = true)
    public String display_name;

    @Column(name="email", unique = true, index = true)
    public String email;

    @Column(name="gender")
    public String gender;

    @Column(name="date_of_birth")
    public Date date_of_birth;

    @Column(name="phone_number")
    public String phone_number;

    @Column(name="phone_owner")
    public boolean phone_owner;

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
        this.phone_owner = false;
    }

    public Friend(FriendJSONModel model)  throws ParseException {
        this.display_name = model.getDisplay_name();
        this.email = model.getEmail();
        this.date_of_birth = model.getDate_of_birth();
        this.gender = model.getGender();
        this.phone_number = model.getPhone_number();
        this.phone_owner = false;
    }

    public String getDisplay_name() {
        return display_name;
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

    public static Friend findOrCreateFromModel(FriendJSONModel model) throws ParseException {
        Friend existingFriend =
                new Select()
                        .from(Friend.class)
                        .where("email = ?", model.getEmail())
                        .executeSingle();
        // If the friend is not there in the local db
        if (existingFriend == null) {
            Friend friend = new Friend(model);
            friend.phone_owner = false;
            friend.save();
            return friend;
        }
        // If the friend already exists in local db
        if((existingFriend.getEmail().equals(model.getEmail())) &&
                existingFriend.getDisplay_name().equals(model.getDisplay_name()) &&
                existingFriend.getGender().equals(model.getGender()) &&
                 existingFriend.getDate_of_birth().equals(model.getDate_of_birth()) &&
                existingFriend.getPhone_number().equals(model.getPhone_number())) {
            return existingFriend;
        } else {
            Friend friend = Friend.load(Friend.class, existingFriend.getId());
            friend.display_name = model.getDisplay_name();
            friend.date_of_birth = model.getDate_of_birth();
            friend.phone_number = model.getPhone_number();
            friend.gender = model.getGender();
            friend.phone_owner = false;
            friend.save();
            return friend;
        }
    }

    /**
     * Deletes all the older friend list when a new user registers and adds
     * the current user as the first friend list in-order to get listed in
     * the event creation
     * @param friendJSONModel
     * @return Friend
     */
    public static Friend registerNewUser(FriendJSONModel friendJSONModel) throws SQLException{
        Friend existingFriend = new Select()
                .from(Friend.class)
                .where(Constants.IF_PHONE_OWNER)
                .executeSingle();
        if (existingFriend != null) {
            existingFriend.phone_owner = false;
            existingFriend.save();
        }
        // Add new user as the first friend
        Friend friend = new Friend();
        friend.display_name = friendJSONModel.getDisplay_name();
        friend.email = friendJSONModel.getEmail();
        friend.date_of_birth = friendJSONModel.getDate_of_birth();
        friend.phone_number = friendJSONModel.getPhone_number();
        friend.gender = friendJSONModel.getGender();
        friend.phone_owner = true;
        friend.save();
        return friend;
    }

    public String getEmail() {
        return email;
    }

    public static Friend getFriendDetails(String email) {
        return new Select().from(Friend.class).where("email = ?", email).executeSingle();

    }

    public static Cursor fetchResultCursor() {
        String tableName = Cache.getTableInfo(Friend.class).getTableName();
        String resultRecords = new Select(tableName + ".*, " + tableName + ".Id as _id").
                from(Friend.class)
                .where(Constants.IF_NOT_PHONE_OWNER)
                .toSql();
        return Cache.openDatabase().rawQuery(resultRecords, null);
    }

}
