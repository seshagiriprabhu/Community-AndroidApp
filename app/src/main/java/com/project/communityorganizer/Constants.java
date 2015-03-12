package com.project.communityorganizer;

/**
 * Created by
 * @author seshagiri on 1/3/15.
 */
public class Constants {
    public static final String TIME_ZONE = "UTC";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DATE_TIME_FORMAT2 = "E yyyy.MM.dd 'at' hh:mm a zzz";
    public static final String DATE_TIME_FORMAT3 = "yyyy-MM-ddHH:mm";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm";

    public static final String SERVER = "http://192.168.1.75:8000/";
    public static final String SERVER2 = "http://10.10.72.137:8000/";
    public static final String URL_REGISTER = "/register/";
    public static final String URL_FRIEND_LIST = "/register/friend_list/{email}/";
    public static final String URL_GEOFENCE_LIST = "/geofence/list";
    public static final String URL_CREATE_EVENT = "/event/";
    public static final String URL_EVENT_OPEN_LIST = "/event/list/open";
    public static final String URL_EVENT_CLOSED_LIST = "/event/list/closed";
    public static final String URL_EVENT_ATTENDANCE = "/event/attendance/";
    public static final String URL_EVENT_ATTENDANCE_LIST = "/event/{event_id}/attendance";

    public static final String EMAIL_QUERY = "email = ";
    public static final String IF_PHONE_OWNER = "phone_owner = 1";
    public static final String IF_NOT_PHONE_OWNER = "phone_owner = 0";

    public static final String NOT_CONNECTED = "You are not connected to internet";
    public static final String NA = "NA";
}
