package com.project.communityorganizer;

import android.app.ListActivity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import static com.project.communityorganizer.sqlite.models.User.checkCredentials;

public class SignInActivity extends Activity  implements OnClickListener {

	public static final String AUTHENTICATION_FAILED = "0";
	public static final String FRIEND_LIST = "FRIEND_LIST";
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
        return networkInfo != null && networkInfo.isConnected();
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
                    task.execute(email, password);
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
    public void onClick(View v) {

    }

    /* Asyncronous task to validate user credentials from local db */
    private class validateUserTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... params) {
            return checkCredentials(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            if (s) {
                Intent i = new Intent(SignInActivity.this, ListActivity.class);
                startActivity(i);
            } else {
                outText.setText("Sorry!! Incorrect Username or Password");
            }
        }
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
