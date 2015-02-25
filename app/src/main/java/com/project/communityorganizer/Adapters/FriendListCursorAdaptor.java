package com.project.communityorganizer.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.communityorganizer.R;

/**
 * Created by
 * @author seshagiri on 25/2/15.
 */
public class FriendListCursorAdaptor extends CursorAdapter {
    final Integer[] imgId = new Integer[]{
            R.drawable.ic_action_user,
            R.drawable.ic_action_friend
    };

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
        ViewHolder holder = new ViewHolder();
        holder.displayName = (TextView) view.findViewById(R.id.displayName);
        holder.imageView = (ImageView) view.findViewById(R.id.friend_icon);
        holder.email = (TextView) view.findViewById(R.id.email);
        holder.displayName.setText(cursor.getString(cursor.getColumnIndexOrThrow("display_name")));
        holder.email.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        String gender = cursor.getString(cursor.getColumnIndex("gender"));
        if (gender.equals("M")) {
            holder.imageView.setImageResource(imgId[0]);
        } else {
            holder.imageView.setImageResource(imgId[1]);
        }
        view.setTag(holder);
    }

    private static class ViewHolder {
        public TextView displayName;
        public ImageView imageView;
        public TextView email;
    }
}
