package com.project.communityorganizer.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.communityorganizer.JSON.models.FriendJSONModel;
import com.project.communityorganizer.R;

/**
 * Created by
 * @author seshagiri on 25/2/15.
 */
public class FriendListCursorAdaptor extends CursorAdapter {
    FriendJSONModel friendJSONModel;

    public FriendListCursorAdaptor(Context context, Cursor c) {
        super(context, c, 0);
        // TODO Auto-generated constructor stub
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.friend_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final Integer[] imgId = new Integer[]{
                R.mipmap.ic_launcher_male,
                R.mipmap.ic_launcher_female,
                R.drawable.ic_action_user
        };

        ViewHolder holder = new ViewHolder();
        friendJSONModel = new FriendJSONModel();
        friendJSONModel.display_name = cursor.getString(cursor.getColumnIndexOrThrow("display_name"));
        friendJSONModel.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
        friendJSONModel.gender = cursor.getString(cursor.getColumnIndex("gender"));
        friendJSONModel.phone_number = cursor.getString(cursor.getColumnIndex("phone_number"));

        holder.displayName = (TextView) view.findViewById(R.id.displayName);
        holder.imageView = (ImageView) view.findViewById(R.id.friend_icon);
        holder.email = (TextView) view.findViewById(R.id.email);
        holder.displayName.setText(friendJSONModel.getDisplay_name());
        holder.email.setText(friendJSONModel.getEmail());

        switch (friendJSONModel.getGender()) {
            case "M":
                holder.imageView.setImageResource(imgId[0]);
                break;
            case "F":
                holder.imageView.setImageResource(imgId[1]);
                break;
            default:
                holder.imageView.setImageResource(imgId[2]);
                break;
        }
        view.setTag(holder);
    }

    private static class ViewHolder {
        public TextView displayName;
        public ImageView imageView;
        public TextView email;
    }
}
