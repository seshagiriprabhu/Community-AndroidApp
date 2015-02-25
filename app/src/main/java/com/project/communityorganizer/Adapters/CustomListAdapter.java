package com.project.communityorganizer.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.communityorganizer.R;


/**
 * Created by
 * @author seshagiri on 24/2/15.
 */
public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] itemName;
    private final Integer[] imgId;
    private final String[] itemDescription;

    public CustomListAdapter(Activity context,
                             String[] itemName,
                             Integer[] imgId,
                             String[] itemDescription) {
        super(context, R.layout.home_activity_list, itemName);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.itemName = itemName;
        this.imgId = imgId;
        this.itemDescription = itemDescription;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = context.getLayoutInflater();
        view = inflater.inflate(R.layout.home_activity_list, parent, false);
        holder.textView = (TextView) view.findViewById(R.id.itemName);
        holder.imageView = (ImageView) view.findViewById(R.id.icon);
        holder.description = (TextView) view.findViewById(R.id.textView1);
        holder.textView.setText(itemName[position]);
        holder.imageView.setImageResource(imgId[position]);
        holder.description.setText(itemDescription[position]);
        view.setTag(holder);
        return view;
    }

    private static class ViewHolder {
        public TextView textView;
        public ImageView imageView;
        public TextView description;
    }

}
