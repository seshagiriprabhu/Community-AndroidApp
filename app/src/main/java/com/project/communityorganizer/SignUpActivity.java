package com.project.communityorganizer;

/* Android in-build libs and apps */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ProgressBar;
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
import org.json.JSONObject;

/* Java libraries */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class SignUpActivity extends Activity {
    private EditText displayNameText, eMailText, passwordText, passwordAgainText;
    private EditText genderText, DOBText, phoneNumberText;
    private Button btnSignUp, btnCancel;
    private ProgressBar pb;

    private static final int FILL_ALL_FIELDS = 0;
    protected static final int TYPE_SAME_PASSWORD_IN_PASSWORD_FIELDS = 1;
    private static final int SIGN_UP_SUCCESSFULL = 4;
    protected static final int USERNAME_AND_PASSWORD_LENGTH_SHORT = 5;

    private static String domainURL = "http://134.76.249.227/";
    private static String registerURL = domainURL + "register/";
    private static String friend_listURL = registerURL + "friend_list/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign up");

        displayNameText = (EditText) findViewById(R.id.etUserName);
        eMailText = (EditText) findViewById(R.id.etEmail);
        passwordText = (EditText) findViewById(R.id.etPass);
        passwordAgainText = (EditText) findViewById(R.id.etPassagn);
        genderText = (EditText) findViewById(R.id.etGender);
        DOBText = (EditText) findViewById(R.id.etdob);
        phoneNumberText = (EditText) findViewById(R.id.etphone_no);
        pb=(ProgressBar)findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);

        Context context = getApplicationContext();
        CharSequence connected = "You're connected to internet";
        CharSequence notConnected = "You're not connected to internet";
        int duration = Toast.LENGTH_SHORT;

        /* Check if the phone is connected to internet */
        if (isConnected()) {
            Toast toast = Toast.makeText(context, connected, duration);
            toast.show();
        } else {
            Toast toast = Toast.makeText(context, notConnected, duration);
            toast.show();
        }
        btnClick();
    }

    private void btnClick() {
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // TODO user input validation
                if(!validate()) {
                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                } else {
                    new JSONAsyncTask().execute(registerURL);
                }
            }

        });

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new OnClickListener(){
            public void onClick(View arg0)
            {
                finish();
            }
        });
    }


    private class JSONAsyncTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... url) {
            User user = new User();
            user.setEmail(eMailText.getText().toString());
            user.setDisplay_name(displayNameText.getText().toString());
            user.setPassword(passwordText.getText().toString());
            user.setDate_of_birth(DOBText.getText().toString());
            user.setGender(genderText.getText().toString());
            user.setCarrier(getCarrier());
            user.setPhone_uid(getDeviceId());
            user.setPhone_number(getPhoneNumber());
            user.setMobile_os(android.os.Build.VERSION.RELEASE);
            user.setMobile_device(android.os.Build.MODEL);

            String result = postData(url[0], user);
            System.out.println(result);
            return null;
        }

        protected void onPostExecute(Boolean result) {
            pb.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_LONG).show();
            Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(i);
            SignUpActivity.this.finish();
        }

        protected void onProgressUpdate(Integer... progress){
            pb.setProgress(progress[0]);
        }

    }

    public static String postData(String url, User user){
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("display_name", user.getDisplay_name());
            jsonObject.accumulate("email", user.getEmail());
            jsonObject.accumulate("password", user.getPassword());
            jsonObject.accumulate("gender", user.getGender());
            jsonObject.accumulate("date_of_birth", user.getDate_of_birth());
            jsonObject.accumulate("phone_number", user.getPhone_number());
            jsonObject.accumulate("mobile_os", user.getMobile_os());
            jsonObject.accumulate("mobile_device", user.getMobile_device());
            jsonObject.accumulate("phone_uid", user.getPhone_uid());
            jsonObject.accumulate("carrier", user.getCarrier());
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
			case SIGN_UP_SUCCESSFULL:
				return new AlertDialog.Builder(SignUpActivity.this)       
				.setMessage(R.string.signup_successfull)
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finish();
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
        if(displayNameText.getText().toString().trim().equals(""))
            return false;
        else if(eMailText.getText().toString().trim().equals(""))
            return false;
        else if(passwordText.getText().toString().trim().equals(""))
            return false;
        else if(passwordAgainText.getText().toString().trim().equals(""))
            return false;
        else if(genderText.getText().toString().trim().equals(""))
            return false;
        else if(DOBText.getText().toString().trim().equals(""))
            return false;
        else if(phoneNumberText.getText().toString().trim().equals(""))
            return false;
        else
            return true;
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

}