package com.project.communityorganizer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.activeandroid.ActiveAndroid;

import retrofit.RestAdapter;

public class MainActivity extends Activity implements OnClickListener {

	Button btnSignIn;
	Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this);


        setContentView(R.layout.activity_main);
        
        btnSignIn = (Button) findViewById(R.id.btnSingIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        
        
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        
    }
	@Override
	public void onClick(View v) {
		Intent i = null;
		int id = v.getId();
		if (id == R.id.btnSingIn) {
			i = new Intent(this,SignInActivity.class);
		} else if (id == R.id.btnSignUp) {
			i = new Intent(this,SignUpActivity.class);
		}
		startActivity(i);
	}
    
}
