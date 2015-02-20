package com.project.communityorganizer;

import java.io.UnsupportedEncodingException;

import android.app.ListActivity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import com.project.communityorganizer.SignInActivity;
import com.project.communityorganizer.SignUpActivity;
import com.project.communityorganizer.R;
import com.project.communityorganizer.sqlite.models.Friend;
import com.project.communityorganizer.sqlite.models.User;

public class SignInActivity extends Activity  implements OnClickListener {
	
	protected static final int NOT_CONNECTED_TO_SERVICE = 0;
	protected static final int FILL_BOTH_EMAIL_AND_PASSWORD = 1;
	public static final String AUTHENTICATION_FAILED = "0";
	public static final String FRIEND_LIST = "FRIEND_LIST";
	protected static final int MAKE_SURE_EMAIL_AND_PASSWORD_CORRECT = 2 ;
	protected static final int NOT_CONNECTED_TO_NETWORK = 3;
    public static final int SIGN_UP_ID = Menu.FIRST;
    public static final int EXIT_APP_ID = Menu.FIRST + 1;

	private EditText eMailText, passwordText;
    private Button btnCancel, btnSign;
    private TextView outText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        setTitle("Sign in");
        findViewsById();
        showConnectivity();
        btnClick();
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

    /* Handles button click events */
    private void btnClick() {
        btnSign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO user input validation
                if(!validate()) {
                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                } else {
                    String email = eMailText.getText().toString().trim();
                    String password = passwordText.getText().toString();
                    validateUserTask task = new validateUserTask();
                    task.execute(new String[]{email, password});
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

    /* Validates the if the user entered blank inputs */
    private boolean validate() {
        if(eMailText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(),R.string.fill_email_field,
                    Toast.LENGTH_LONG).show();
            return false;
        } else if (passwordText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(),R.string.fill_password_field,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /* FindViewsbyID */
    private void findViewsById() {
        passwordText = (EditText) findViewById(R.id.etPass);
        eMailText = (EditText) findViewById(R.id.etEmail);
        outText = (TextView) findViewById(R.id.txtError);
        btnSign = (Button) findViewById(R.id.btnSignIn);
        btnCancel = (Button) findViewById(R.id.btnCancel);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        int message = -1;
        switch (id) {
            case NOT_CONNECTED_TO_SERVICE:
                message = R.string.not_connected_to_service;
                break;
            case FILL_BOTH_EMAIL_AND_PASSWORD:
                message = R.string.fill_both_email_and_password;
                break;
            case MAKE_SURE_EMAIL_AND_PASSWORD_CORRECT:
                message = R.string.make_sure_email_and_password_correct;
                break;
            case NOT_CONNECTED_TO_NETWORK:
                message = R.string.not_connected_to_network;
                break;
            default:
                break;
        }

        if (message == -1)
        {
            return null;
        }
        else
        {
            return new AlertDialog.Builder(SignInActivity.this)
                    .setMessage(message)
                    .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
       				/* User clicked OK so do some stuff */
                        }
                    })
                    .create();
        }
    }

   	@Override
   	protected void onPause() 
   	{
   		super.onPause();
   	}

   	@Override
   	protected void onResume() 
   	{
   		super.onResume();
   	}

   	@Override
   	public boolean onCreateOptionsMenu(Menu menu) {		
   		boolean result = super.onCreateOptionsMenu(menu);
   		 menu.add(0, SIGN_UP_ID, 0, R.string.sign_up);
   		 menu.add(0, EXIT_APP_ID, 0, R.string.exit_application);
   		return result;
   	}
   	
   	@Override
   	public boolean onMenuItemSelected(int featureId, MenuItem item) {
   		switch(item.getItemId()) {
   	    	case SIGN_UP_ID:
   	    		Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
   	    		startActivity(i);
   	    		return true;
   	    	case EXIT_APP_ID:
   	    		btnCancel.performClick();
   	    		return true;
   	    }
   	    return super.onMenuItemSelected(featureId, item);
   	}

    @Override
    public void onClick(View v) {

    }

    /* Asyncronous task to validate user credentials from local db */
    private class validateUserTask extends AsyncTask<String, Integer, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            User user = User.getLoginDetails(params[0], params[1]);
            return user.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("1")) {
                Intent i = new Intent(SignInActivity.this, ListActivity.class);
                startActivity(i);
            } else {
                outText.setText("Sorry!! Incorrect Username or Password");
            }
        }
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
