package com.project.communityorganizer;

/* Android in-build libs and apps */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.project.communityorganizer.JSON.models.UserJSONModel;
import com.project.communityorganizer.services.RestService;
import com.project.communityorganizer.services.SaveSharedPreference;
import com.project.communityorganizer.services.passwordHash;
import com.project.communityorganizer.sqlite.models.User;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.app.AlertDialog.Builder;
import static com.project.communityorganizer.services.SaveSharedPreference.*;

public class SignUpActivity extends Activity implements OnClickListener {

    final Context context = this;
    private EditText displayNameText, eMailText, passwordText, passwordAgainText;
    private EditText DOBText;
    private Button btnSignUp, btnCancel;
    private RadioButton radioSexButton;
    private DatePickerDialog DOBDatePicker;
    private SimpleDateFormat dateFormatter;
    public static String REGISTRATION_TITLE;
    public static String REGISTRATION_DETAILS;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Register User");
        findViewsById();
        showConnectivity();
        btnClick();
        setDateTimeField();
    }

    private void btnClick() {
        btnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO user input validation
                if (!validate()) {
                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                } else {
                    new JSONAsyncTask().execute();
                }
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * Check if the phone is connected to internet
     */
    private void showConnectivity() {
        Context context = getApplicationContext();
        CharSequence notConnected = "You're not connected to internet";
        int duration = Toast.LENGTH_SHORT;

        if (!isConnected()) {
            Toast toast = Toast.makeText(context, notConnected, duration);
            toast.show();
        }
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

    @Override
    public void onClick(View v) {
        if (v == DOBText) {
            dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
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
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

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
         * Handles user registration
         * @return boolean
         * @throws ParseException
         * @throws JSONException
         * @throws NoSuchAlgorithmException
         */
        private boolean registerUser() throws ParseException, JSONException, NoSuchAlgorithmException {
            final UserJSONModel userJSONModel = new UserJSONModel();
            userJSONModel.setEmail(eMailText.getText().toString());
            userJSONModel.setDisplay_name(displayNameText.getText().toString());
            String plainText = passwordText.getText().toString();
            passwordHash hash = new passwordHash();
            String password = hash.findHash(plainText);
            userJSONModel.setPassword(password);
            String DOB = DOBText.getText().toString();
            Date date = dateFormatter.parse(DOB);
            userJSONModel.setDate_of_birth(date);
            userJSONModel.setPhone_number(getPhoneNumber());
            userJSONModel.setMobile_os(android.os.Build.VERSION.RELEASE);
            userJSONModel.setMobile_device(android.os.Build.MODEL);
            userJSONModel.setPhone_uid(getDeviceId());
            userJSONModel.setCarrier(getCarrier());

            String sex = radioSexButton.getText().toString();
            String gender = "F";
            if (sex.equals("Male")) gender = "M";

            userJSONModel.setGender(gender);
            final RestService restService = new RestService();
            RestService.CommunityAppWebService appWebService = restService.getService();
            appWebService.registerUser(userJSONModel,
                    new Callback<UserJSONModel>() {
                        @Override
                        public void success(UserJSONModel model, Response response) {
                            if (response.getStatus() == 201) {
                                try {
                                    User user = User.findOrCreateFromJson(model);
                                    user.save();
                                    SaveSharedPreference
                                            .setUserEmail(SignUpActivity.this,
                                                    model.getEmail());
                                    SaveSharedPreference
                                            .setUserName(SignUpActivity.this,
                                                    model.getDisplay_name());
                                    restService.fetchFriendList(model.getEmail());
                                    restService.fetchGeofenceList();
                                    REGISTRATION_TITLE = "Success";
                                    REGISTRATION_DETAILS = "User Registered!";
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                    REGISTRATION_TITLE = "Error";
                                    REGISTRATION_DETAILS = "Something wrong in the server side";
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            // TODO error verbose
                            REGISTRATION_TITLE = "Registration Failed";
                            REGISTRATION_DETAILS ="Email/Display already in use";
                        }
                    });
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (result) postRegistrationDialog();
        }

    }
    /**
     * Dialog to be shown after the registration
     */
    public void  postRegistrationDialog() {
        if (!getUserEmail(SignUpActivity.this).equals("[]")) {
            startActivity(new Intent(this, SignInActivity.class));
            SignUpActivity.this.finish();
        }

        Builder alertDialogBuilder = new Builder(
                context);
        alertDialogBuilder.setTitle(REGISTRATION_TITLE);
        alertDialogBuilder
                .setMessage(REGISTRATION_DETAILS)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .create();
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     *  Checks if the device is connected to internet
     */
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
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
     * Returns the Phone UID
     * @return String
     */
    private String getDeviceId() {
        String deviceId;
        final TelephonyManager mTelephony = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null) {
            deviceId = mTelephony.getDeviceId();
        } else {
            deviceId = Settings.Secure.getString(getApplicationContext()
                    .getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }

    /**
     * Retrieves the carrier ID of the phone
     * @return String
     */
    private String getCarrier() {
        TelephonyManager tManager = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tManager.getNetworkOperatorName();
    }

    /**
     * Retrieves the phone number in use
     * @return String
     */
    private String getPhoneNumber() {
        TelephonyManager tManager = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tManager.getLine1Number();
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