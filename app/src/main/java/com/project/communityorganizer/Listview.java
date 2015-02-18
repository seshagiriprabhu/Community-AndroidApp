package com.project.communityorganizer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Listview extends ListActivity{
	
	String classes[]= { "Geofencing","AddFriend","PendingRequest","FriendOnline","EditFriends","EventCreation",
			             "EventAttendance","EditEventDetails"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(Listview.this, android.R.layout.simple_list_item_1, classes));
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String item = classes[position];
		try{
			Class<?> ourClass = Class.forName("com.project.communityorganizer." + item);
			Intent ourIntent = new Intent(Listview.this, ourClass);
			startActivity(ourIntent);
		   }catch(ClassNotFoundException e){
			    e.printStackTrace();
			
		}
	}
}
