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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.project.communityorganizer.sqlite.models.Friend;
import com.project.communityorganizer.sqlite.models.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Response;

import static android.app.AlertDialog.Builder;

public class SignUpActivity extends Activity implements OnClickListener {

    final Context context = this;
    private EditText displayNameText, eMailText, passwordText, passwordAgainText;
    private EditText DOBText;
    private Button btnSignUp, btnCancel;
    private RadioButton radioSexButton;
    private DatePickerDialog DOBDatePicker;
    private SimpleDateFormat dateFormatter;

    private static String domainURL = "http://134.76.249.227/";
    private static String registerURL = domainURL + "register/";
    private static String friend_listURL = registerURL + "friend_list/";
    private static String format_json = "/?format=json";
    private static String userEmail;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign up");
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
                if(!validate()) {
                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                } else {
                    new JSONAsyncTask().execute(registerURL);
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


    /* Check if the phone is connected to internet */
    private void showConnectivity() {
        Context context = getApplicationContext();
        CharSequence connected = "You're connected to internet";
        CharSequence notConnected = "You're not connected to internet";
        int duration = Toast.LENGTH_SHORT;

        if (isConnected()) {
            Toast toast = Toast.makeText(context, connected, duration);
            toast.show();
        } else {
            Toast toast = Toast.makeText(context, notConnected, duration);
            toast.show();
        }
    }

    /* Identifies elements in the view by its ID */
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

    private class JSONAsyncTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... url) {
            if (registerUser(url)) {
                try {
                    return fetchFriendList();
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            } else {
                return false;
            }
            return null;
        }

        private boolean registerUser(String[] url) {
            String email = eMailText.getText().toString();
            String display_name = displayNameText.getText().toString();
            String password = passwordText.getText().toString();
            String DOB = DOBText.getText().toString();
            String phoneNumber = getPhoneNumber();
            String mobile_os = android.os.Build.VERSION.RELEASE;
            String mobile_device = android.os.Build.MODEL;
            String phone_uid = getDeviceId();
            String carrier = getCarrier();

            String sex = radioSexButton.getText().toString();
            String gender = "F";
            if (sex.equals("Male")) gender = "M";

            String result = postData(url[0], email, display_name, password, DOB, gender,
                    phoneNumber, phone_uid, mobile_device, mobile_os, carrier);

            try {
                if (check_result(result, email, display_name, DOB, gender, phoneNumber,
                        mobile_device, mobile_os, carrier)) {
                    saveResult(result, password);
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
            System.out.println(result);
            return false;

        }

        private boolean fetchFriendList() throws JSONException, ParseException {
            return true;
            //return updateFriendTable(result);
        }

        private boolean updateFriendTable(String result) throws JSONException, ParseException {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("email");
            ActiveAndroid.beginTransaction();
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Friend friend = Friend.findOrCreateFromJson(obj);
                    friend.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(i);
                SignUpActivity.this.finish();
                Builder alertDialogBuilder = new Builder(
                        context);
                alertDialogBuilder.setTitle(R.string.Congrats);
                alertDialogBuilder
                        .setMessage(R.string.Congrats)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                SignUpActivity.this.finish();
                            }
                        })
                        .create();
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            else {
                Builder alertDialogBuilder = new Builder(
                        context);
                alertDialogBuilder.setTitle(R.string.registration_failed);
                alertDialogBuilder
                        .setMessage(R.string.registration_failed_reason)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }

        protected void onProgressUpdate(Integer... progress){
        }

    }


    private User saveResult(String result, String password) throws JSONException, ParseException {
        JSONObject obj = new JSONObject(result);
        obj.accumulate("password", password);
        User user = User.registerNewUser(obj);
        userEmail = user.getEmail();
        System.out.println(userEmail);
        return user;
    }

    private boolean check_result(String result, String email, String display_name,
                                 String dob, String gender, String phonenumber,
                                 String mobile_device, String mobile_os, String carrier)
            throws JSONException, ParseException
    {

        final JSONObject obj = new JSONObject(result);

        if (display_name.equals(obj.getString("display_name")) &&
                email.equals(obj.getString("email")) &&
                gender.equals(obj.getString("gender")) &&
                dob.equals(obj.getString("date_of_birth")) &&
                phonenumber.equals(obj.getString("phone_number")) &&
                mobile_device.equals(obj.getString("mobile_device")) &&
                mobile_os.equals(obj.getString("mobile_os")) &&
                carrier.equals(obj.getString("carrier"))) {
            return true;
        }

        if ("This field must be unique.".equals(obj.getString("display_name"))) {
            return false;
        }

        if (obj.getString("email").equals("This field must be unique.")) {
            return false;
        }
        return false;
    }

    private String postData(String url, String email, String display_name, String password,
                            String dob, String gender, String phonenumber,
                            String phone_uid, String mobile_device, String mobile_os,
                            String carrier)
    {

        InputStream inputStream;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            String json;
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("display_name", display_name);
            jsonObject.accumulate("email", email);
            jsonObject.accumulate("password", password);
            jsonObject.accumulate("gender", gender);
            jsonObject.accumulate("date_of_birth", dob);
            jsonObject.accumulate("phone_number", phonenumber);
            jsonObject.accumulate("mobile_os", mobile_os);
            jsonObject.accumulate("mobile_device", mobile_device);
            jsonObject.accumulate("phone_uid", phone_uid);
            jsonObject.accumulate("carrier", carrier);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + httpResponse.getStatusLine().getStatusCode());
            }

            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null) result = convertInputStreamToString(inputStream);
            else {
                result = "Registration Failed";
            }
            httpclient.getConnectionManager().shutdown();
        } catch (ClientProtocolException e) {
            Log.d("Client exception", e.getLocalizedMessage());
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    /* Checks if the device is connected to internet */
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


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

    /* Converts the raw stream of data to String */
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    /* Returns the Phone UID */
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

    /* Retrieves the carrier ID of the phone */
    private String getCarrier() {
        TelephonyManager tManager = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tManager.getNetworkOperatorName();
    }

    /* Retrieves the phone number used by the device */
    private String getPhoneNumber() {
        TelephonyManager tManager = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tManager.getLine1Number();
    }


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