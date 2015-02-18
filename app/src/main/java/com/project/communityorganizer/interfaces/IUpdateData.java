package com.project.communityorganizer.interfaces;
import com.project.communityorganizer.types.FriendInfo;
import com.project.communityorganizer.types.MessageInfo;


public interface IUpdateData {
	public void updateData(MessageInfo[] messages, FriendInfo[] friends, FriendInfo[] unApprovedFriends, String userKey);

}
