package com.project.communityorganizer.Activities;

/* Android in-build libs and apps */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.project.communityorganizer.Constants;
import com.project.communityorganizer.JSON.models.FriendJSONModel;
import com.project.communityorganizer.JSON.models.UserJSONModel;
import com.project.communityorganizer.R;
import com.project.communityorganizer.Services.DeviceManager;
import com.project.communityorganizer.Services.RestService;
import com.project.communityorganizer.Services.passwordHash;
import com.project.communityorganizer.sqlite.models.Friend;
import com.project.communityorganizer.sqlite.models.User;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by
 * @author seshagiri on 19/2/15.
 */
public class SignUpActivity extends Activity implements OnClickListener {
    final Context context = this;
    DeviceManager deviceManager = new DeviceManager();
    private EditText displayNameText, eMailText, passwordText, passwordAgainText;
    private EditText DOBText;
    private Button btnSignUp, btnCancel;
    private RadioButton radioSexButton;
    private DatePickerDialog DOBDatePicker;
    private SimpleDateFormat dateFormatter;
    public static String REGISTRATION_TITLE;
    public static String REGISTRATION_DETAILS;
    public static boolean registration_status = false;

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Register User");
        findViewsById();
        deviceManager.showConnectivity(getApplicationContext());
        btnClick();
        setDateTimeField();
    }

    /**
     * A function to handle Button click events
     */
    private void btnClick() {
        btnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) new JSONAsyncTask(context).execute();
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(SignUpActivity.this);
            }
        });
    }

    /**
     * Identifies elements in the view by its ID
     */
    private void findViewsById() {
        displayNameText = (EditText) findViewById(R.id.etUserName);
        eMailText = (EditText) findViewById(R.id.etEmail);
        passwordText = (EditText) findViewById(R.id.etPass);
        passwordAgainText = (EditText) findViewById(R.id.etPassagn);
        DOBText = (EditText) findViewById(R.id.etdob);
        RadioGroup radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnCancel = (Button) findViewById(R.id.btnCancel);
    }

    /**
     * {@inheritDoc}
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == DOBText) {
            dateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DOBDatePicker.getDatePicker().updateDate(year, month, day);
            DOBDatePicker.show();
        }
    }

    /**
     * Asynchronous task to register, fetch friend list and geofence list
     */
    private class JSONAsyncTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
        AlertDialog.Builder alertDialog;
        Context context;

        private JSONAsyncTask(Context context){
            this.context = context.getApplicationContext();
            dialog.setCancelable(false);
            dialog.setTitle("Please wait");
            dialog.setMessage("Registration in progress");
            dialog.show();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        /**
         * {@inheritDoc}
         * @param url
         * @return
         */
        @Override
        protected Boolean doInBackground(String... url) {
            try {
                return registerUser();
            } catch (ParseException | JSONException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * {@inheritDoc}
         * @param result
         */
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (dialog.isShowing())
                dialog.dismiss();

            alertDialog = new AlertDialog.Builder(SignUpActivity.this);
            alertDialog.setTitle(REGISTRATION_TITLE);
            alertDialog.setMessage(REGISTRATION_DETAILS);
            if (registration_status) {
                alertDialog.setIcon(R.drawable.ic_action_done);
                alertDialog.setPositiveButton(R.string.OK,
                        new DialogInterface.OnClickListener() {
                            /**
                             * {@inheritDoc}
                             * @param dialog
                             * @param which
                             */
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                SignUpActivity.this.finish();
                            }
                        });
            } else {
                alertDialog.setIcon(R.drawable.ic_action_delete);
                alertDialog.setPositiveButton(R.string.OK,
                        new DialogInterface.OnClickListener() {
                            /**
                             * {@inheritDoc}
                             * @param dialog
                             * @param which
                             */
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
            AlertDialog alertDialog1 = alertDialog.create();
            alertDialog1.show();
        }
    }

    /**
     * Handles user registration
     * @return boolean
     * @throws ParseException
     * @throws JSONException
     * @throws NoSuchAlgorithmException
     */
    private boolean registerUser() throws ParseException, JSONException, NoSuchAlgorithmException {
        final UserJSONModel userJSONModel = new UserJSONModel();
        final FriendJSONModel friendJSONModel = new FriendJSONModel();

        userJSONModel.setEmail(eMailText.getText().toString());
        userJSONModel.setDisplay_name(displayNameText.getText().toString());
        friendJSONModel.display_name = userJSONModel.getDisplay_name();

        String plainText = passwordText.getText().toString();
        passwordHash hash = new passwordHash();
        String password = hash.findHash(plainText);
        userJSONModel.setPassword(password);

        userJSONModel.setDate_of_birth_from_utc(DOBText.getText().toString());
        userJSONModel.setPhone_number(deviceManager.getPhoneNumber(context));
        userJSONModel.setMobile_os(android.os.Build.VERSION.RELEASE);
        userJSONModel.setMobile_device(android.os.Build.MODEL);
        userJSONModel.setPhone_uid(deviceManager.getDeviceId(context));
        userJSONModel.setCarrier(deviceManager.getCarrier(context));

        String sex = radioSexButton.getText().toString();
        String gender = "F";
        if (sex.equals("Male")) gender = "M";
        userJSONModel.setGender(gender);

        final RestService restService = new RestService();
        RestService.CommunityAppWebService appWebService = restService.getService();
        appWebService.registerUser(userJSONModel,
                new Callback<UserJSONModel>() {
                    /**
                     * {@inheritDoc}
                     * @param model
                     * @param response
                     */
                    @Override
                    public void success(UserJSONModel model, Response response) {
                        if (response.getStatus() == 201) {
                            friendJSONModel.setEmail(model.getEmail());
                            friendJSONModel.setDisplay_name(model.getDisplay_name());
                            friendJSONModel.setDate_of_birth(model.getDate_of_birth());
                            friendJSONModel.setGender(model.getGender());
                            friendJSONModel.setPhone_number(model.getPhone_number());
                            try {
                                User user = User.findOrCreateFromJson(model);
                                user.save();
                                Friend.registerNewUser(friendJSONModel);
                                restService.fetchFriendList(model.getEmail());
                                restService.fetchGeofenceList();
                                REGISTRATION_TITLE = "Success";
                                REGISTRATION_DETAILS = "User Registered!";
                                registration_status = true;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else {
                            REGISTRATION_TITLE = "Error";
                            REGISTRATION_DETAILS = "Something wrong in the server side";
                        }
                    }

                    /**
                     * {@inheritDoc}
                     * @param error
                     */
                    @Override
                    public void failure(RetrofitError error) {
                        // TODO error verbose
                        REGISTRATION_TITLE = "Registration Failed";
                        REGISTRATION_DETAILS ="Email/Display already in use";
                    }
                });
        return true;
    }

    /**
     * Validates form
     * @return boolean
     */
    private boolean validate(){
        if(displayNameText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(),R.string.fill_display_field,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        else if(eMailText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(),R.string.fill_email_field,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        else if(passwordText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(),R.string.fill_password_field,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        else if(passwordAgainText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(),R.string.fill_repassword_field,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        else if(DOBText.getText().toString().trim().equals(""))
            return false;
        if (passwordText.getText().toString().equals(passwordAgainText.getText().toString())) {
            if (displayNameText.length() >= 5 && passwordText.length() >= 5) {
                return true;
            } else {
                Toast.makeText(getApplicationContext(), R.string.username_and_password_length_short,
                        Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.signup_type_same_password_in_password_fields,
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     *  Sets the Date in the form while using date picker
     */
    private void setDateTimeField() {
        DOBText.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        DOBDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                DOBText.setText(dateFormatter.format(newDate.getTime()));
                System.out.println(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR),
          newCalendar.get(Calendar.MONTH),
          newCalendar.get(Calendar.DAY_OF_MONTH));
    }
}