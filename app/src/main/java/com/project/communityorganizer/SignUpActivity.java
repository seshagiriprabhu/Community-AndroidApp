package com.project.communityorganizer;

/* Android in-build libs and apps */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/* User defined classes */
import com.project.communityorganizer.sqlite.models.User;
import com.project.communityorganizer.R;
import com.project.communityorganizer.SignUpActivity;
import com.project.communityorganizer.SignInActivity;

/* Web related libraries */
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* Java libraries */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class SignUpActivity extends Activity implements OnClickListener{
    private EditText displayNameText, eMailText, passwordText, passwordAgainText;
    private EditText genderText, DOBText, phoneNumberText;
    private Button btnSignUp, btnCancel;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private DatePickerDialog DOBDatePicker;
    private ProgressBar pb;

    private SimpleDateFormat dateFormatter;

    private static final int FILL_ALL_FIELDS = 0;
    protected static final int TYPE_SAME_PASSWORD_IN_PASSWORD_FIELDS = 1;
    private static final int FILL_EMAIL_FIELD = 2;
    private static final int FILL_DISPLAY_NAME_FIELD = 3;
    private static final int FILL_PASSWORD_FIELD = 6;
    private static final int FILL_REPASSWORD_FIELD = 7;
    protected static final int USERNAME_AND_PASSWORD_LENGTH_SHORT = 5;

    private static String domainURL = "http://134.76.249.227/";
    private static String registerURL = domainURL + "register/";
    private static String friend_listURL = registerURL + "friend_list/";

    User user = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign up");
        findViewsById();
        pb.setVisibility(View.GONE);
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
        genderText = (EditText) findViewById(R.id.etGender);
        DOBText = (EditText) findViewById(R.id.etdob);
        phoneNumberText = (EditText) findViewById(R.id.etphone_no);
        pb=(ProgressBar)findViewById(R.id.progressBar1);
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
            String email = eMailText.getText().toString();
            String display_name = displayNameText.getText().toString();
            String password = passwordText.getText().toString();
            String DOB = DOBText.getText().toString();
            String phonenumber = getPhoneNumber();
            String mobile_os = android.os.Build.VERSION.RELEASE;
            String mobile_device = android.os.Build.MODEL;
            String phone_uid = getDeviceId();
            String carrier = getCarrier();


            Date date = null;
            try {
                date = dateFormatter.parse(DOB);
            } catch (ParseException e) {
                date = null;
            }

            String gender = genderText.getText().toString();

            String result = postData(url[0], email, display_name, password, DOB, date, gender,
                    phonenumber, phone_uid, mobile_device, mobile_os, carrier);
            try {
                if (check_result(result, email, display_name, date, gender, phonenumber, phone_uid,
                        mobile_device, mobile_os, carrier)) {
                    saveresult(result, password);
                    Toast.makeText(getApplicationContext(), "User Registered",
                            Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(result);
            return null;
        }

        protected void onPostExecute(Boolean result) {
            pb.setVisibility(View.GONE);
            Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(i);
            SignUpActivity.this.finish();
        }

        protected void onProgressUpdate(Integer... progress){
            pb.setProgress(progress[0]);
        }

    }

    private void saveresult(String result, String password) throws JSONException, ParseException {
        JSONObject obj = new JSONObject(result);
        user.setUid(obj.getInt("uid"));
        user.setDisplay_name(obj.getString("display_name"));
        user.setEmail(obj.getString("email"));
        user.setPassword(password);
        user.setGender(obj.getString("gender"));
        String DOB = obj.getString("date_of_birth");
        Date date = dateFormatter.parse(DOB);
        user.setDate_of_birth(date);
        user.setPhone_number(obj.getString("phone_number"));
        user.setMobile_device(obj.getString("mobile_device"));
        user.setMobile_os(obj.getString("mobile_os"));
        user.setCarrier(obj.getString("carrier"));
    }

    private boolean check_result(String result, String email, String display_name,
                                 Date date, String gender, String phonenumber, String phone_uid,
                                 String mobile_device, String mobile_os, String carrier)
            throws JSONException, ParseException
    {

        final JSONObject obj = new JSONObject(result);
        String DOB = obj.getString("date_of_birth");
        Date date_dob = dateFormatter.parse(DOB);

        if (display_name == obj.getString("display_name") &&
                email == obj.getString("email") &&
                gender == obj.getString("gender") &&
                date == date_dob &&
                phonenumber == obj.getString("phone_number") &&
                mobile_device == obj.getString("mobile_device") &&
                mobile_os == obj.getString("mobile_os") &&
                carrier == obj.getString("carrier"))
            return true;
        if (obj.getString("display_name") == "This field must be unique.") {
            Toast.makeText(getApplicationContext(), "Display name already in use",
                    Toast.LENGTH_LONG).show();
        }
        if (obj.getString("email") == "This field must be unique.") {
            Toast.makeText(getApplicationContext(), "Email already in use",
                    Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private String postData(String url, String email, String display_name, String password,
                            String dob, Date date, String gender, String phonenumber,
                            String phone_uid, String mobile_device, String mobile_os,
                            String carrier)
    {

        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            String json = "";
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

            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
            }
            else {
                result = "Registration Failed";
            }
        } catch (ClientProtocolException e) {
            Log.d("Client exception", e.getLocalizedMessage());
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    @Override
	protected Dialog onCreateDialog(int id) 
	{
		switch (id) 
		{
			case TYPE_SAME_PASSWORD_IN_PASSWORD_FIELDS:			
				return new AlertDialog.Builder(SignUpActivity.this)       
				.setMessage(R.string.signup_type_same_password_in_password_fields)
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/* User clicked OK so do some stuff */
					}
				})        
				.create();			
			case FILL_ALL_FIELDS:				
				return new AlertDialog.Builder(SignUpActivity.this)       
				.setMessage(R.string.signup_fill_all_fields)
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/* User clicked OK so do some stuff */
					}
				})        
				.create();
			case USERNAME_AND_PASSWORD_LENGTH_SHORT:
				return new AlertDialog.Builder(SignUpActivity.this)       
				.setMessage(R.string.username_and_password_length_short)
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/* User clicked OK so do some stuff */
					}
				})        
				.create();
            case FILL_DISPLAY_NAME_FIELD:
                return new AlertDialog.Builder(SignUpActivity.this)
                        .setMessage(R.string.fill_display_field)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                             }
                        })
                        .create();
            case FILL_EMAIL_FIELD:
                return new AlertDialog.Builder(SignUpActivity.this)
                        .setMessage(R.string.fill_email_field)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .create();
            case FILL_PASSWORD_FIELD:
                return new AlertDialog.Builder(SignUpActivity.this)
                        .setMessage(R.string.fill_password_field)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .create();
            case FILL_REPASSWORD_FIELD:
                return new AlertDialog.Builder(SignUpActivity.this)
                        .setMessage(R.string.fill_repassword_field)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .create();
			default:
				return null;
				
		}
	}

    /* Checks if the device is connected to internet */
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
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
        else if(genderText.getText().toString().trim().equals(""))
            return false;
        else if(DOBText.getText().toString().trim().equals(""))
            return false;
        else if(phoneNumberText.getText().toString().trim().equals(""))
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
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    /* Returns the Phone UID */
    private String getDeviceId() {
        String deviceId = "";
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
        String carrierName = tManager.getNetworkOperatorName();
        return carrierName;
    }

    /* Retrieves the phone number used by the device */
    private String getPhoneNumber() {
        TelephonyManager tManager = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tManager.getLine1Number();
        return phoneNumber;
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