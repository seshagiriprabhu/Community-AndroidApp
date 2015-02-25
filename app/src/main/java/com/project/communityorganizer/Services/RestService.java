package com.project.communityorganizer.Services;

import com.activeandroid.ActiveAndroid;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.communityorganizer.JSON.models.FriendJSONModel;
import com.project.communityorganizer.JSON.models.GeofenceJSONModel;
import com.project.communityorganizer.JSON.models.UserJSONModel;
import com.project.communityorganizer.sqlite.models.Friend;
import com.project.communityorganizer.sqlite.models.Geofence;

import java.text.ParseException;
import java.util.List;

import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;


/**
 * Created by
 * @author Seshagiri on 21/2/15.
 */
public class RestService {
    private static final String SERVER = "http://192.168.1.75:8000/";
    private final CommunityAppWebService mCommunityAppWebService;

    /**
     * A service to REST-ful client operations
     */
    public RestService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SERVER)
                .setErrorHandler(new myErrorHandler())
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(new SessionRequestInterceptor())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mCommunityAppWebService = restAdapter.create(CommunityAppWebService.class);
    }

    /**
     * A class for Session Request Interceptor
     */
    public class SessionRequestInterceptor implements RequestInterceptor {
        @Override
        public void intercept(RequestFacade request) {
            request.addHeader("Accept", "application/json");
        }
    }

    /**
     * A function to return the service running
     * @return CommunityAppWebService
     */
    public CommunityAppWebService getService() {
        return mCommunityAppWebService;
    }

    /**
     * Community App Web interface
     */
    public interface CommunityAppWebService {
        /**
         * User registration
         * @param userJSONModel
         * @param callback
         */
        @POST("/register/")
        public void registerUser(
                @Body UserJSONModel userJSONModel,
                Callback<UserJSONModel> callback);

        /**
         * Friend list fetcher
         */
        @GET("/register/friend_list/{email}/")
        public void fetchFriendList(
                @Path("email") String email,
                Callback<List<FriendJSONModel>> callback);

        /**
         *  Geofence list fetcher
         */
        @GET("/geofence/list")
        public void fetchGeofenceList(
                Callback<List <GeofenceJSONModel>> callback);
    }

    /**
     * Function to get the list of friends
     * @param email
     */
    public void fetchFriendList(String email) {
        mCommunityAppWebService.fetchFriendList(email,
                new Callback<List<FriendJSONModel>>() {
                    @Override
                    public void success(List<FriendJSONModel> friendJSONModels, Response response) {
                        ActiveAndroid.beginTransaction();
                        try {
                            for (FriendJSONModel friendJSONModel : friendJSONModels) {
                                if (friendJSONModel != null) {
                                    Friend friend = Friend.findOrCreateFromModel(friendJSONModel);
                                    friend.save();
                                }
                            }
                            ActiveAndroid.setTransactionSuccessful();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        } finally {
                            ActiveAndroid.endTransaction();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
    }

    /**
     * function to fetch the list of geofence
     */
    public void fetchGeofenceList() {
        mCommunityAppWebService.fetchGeofenceList(
                new Callback<List<GeofenceJSONModel>>() {
                    @Override
                    public void success(List<GeofenceJSONModel> geofenceJSONModels, Response response) {
                        ActiveAndroid.beginTransaction();
                        try {
                            for (GeofenceJSONModel geofenceJSONModel : geofenceJSONModels) {
                                Geofence geofence = Geofence.findOrCreateFromModel(geofenceJSONModel);
                                geofence.save();
                            }
                            ActiveAndroid.setTransactionSuccessful();
                        } finally {
                            ActiveAndroid.endTransaction();
                        }
                    }
                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );
    }

    /**
     * Custom error handler
     */
    public class myErrorHandler implements ErrorHandler {

        @Override
        public Throwable handleError(RetrofitError cause) {
            Response r = cause.getResponse();
            if (r != null && r.getStatus() == 400) return new Exception(cause);
            else {
                if (r != null && r.getStatus() == 405) {
                    return new Exception(cause);
                }
            }
            return cause;
        }
    }


}