package com.poli.gfx.main;

import com.poli.gfx.R;
import com.poli.gfx.R.layout;
import com.poli.gfx.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class SignupActivity extends Activity {
	
	private TextView mEmailInputView;
	private TextView mEmailErrorView;
	private TextView mPasswordInputView;
	private TextView mPasswordErrorView;
	private TextView mReenterEmailInputView;
	private TextView mReenterEmailErrorView;
	private TextView mReenterPasswordInputView;
	private TextView mReenterPasswordErrorView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        ((TextView) findViewById(R.id.header_title)).setText("Sign Up");
        
        
        mEmailInputView = (TextView) findViewById(R.id.signup_field_username);
        mEmailErrorView = (TextView) findViewById(R.id.signup_email_error);
        mPasswordInputView = (TextView) findViewById(R.id.signup_field_password);
        mPasswordErrorView = (TextView) findViewById(R.id.signup_password_error);
        mReenterEmailInputView = (TextView) findViewById(R.id.signup_field_reenter_email);
        mReenterEmailErrorView = (TextView) findViewById(R.id.signup_reenter_email_error);
        mReenterPasswordInputView = (TextView) findViewById(R.id.signup_field_reenter_password);
        mReenterPasswordErrorView = (TextView) findViewById(R.id.signup_reenter_password_error);
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_signup, menu);
        return true;
    }
    
    public void signUpButtonClicked (View v){
    	signUpAction();
    }
    
    private void signUpAction(){
    	
    }
    
    
    
}
