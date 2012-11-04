package com.poli.gfx.main;

import com.poli.gfx.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class LoginMainActivity extends Activity {

	private TextView mEmailErrorView;
	private TextView mPasswordErrorView;
	private TextView mEmailInputView;
	private TextView mPasswordInputView;
	
	private ProgressDialog mProgressDialog;
	
	//Activity lifecycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        ((TextView) findViewById(R.id.header_title)).setText("Login");
        
        mEmailErrorView = (TextView) findViewById(R.id.login_email_error);
        mPasswordErrorView = (TextView) findViewById(R.id.login_password_error);
        mEmailInputView = (TextView) findViewById(R.id.login_field_username);
        mPasswordInputView = (TextView) findViewById(R.id.login_field_password);
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login_main, menu);
        return true;
    }
    
    
    
    //Buttons
    public void forgotPasswordButtonClicked (View v){
    	forgotPasswordAction();
    }
    
    public void signInButtonClicked (View v){
    	signInAction();
    }
    
    public void signUpButtonClicked (View v){
    	signUpAction();
    }
    
    private void forgotPasswordAction(){
    	mPasswordErrorView.setVisibility(View.GONE);
    	String email = getEmail();
    	if(email.contains("@")){
    		mEmailErrorView.setVisibility(View.GONE);
    		sendForgotPasswordEmail(email);
    	}
    	else{
    		mEmailErrorView.setVisibility(View.VISIBLE);
    	}
    }
    
    private void signInAction(){
    	String email = getEmail();
    	String password = getPassword();
    	if(email.length() <= 0 || !email.contains("@")){
    		//invalid email
    		mEmailErrorView.setVisibility(View.VISIBLE);
    	}
    	else if (password.length() < 6){
    		//Invalid password
    		mEmailErrorView.setVisibility(View.GONE);
    		mPasswordErrorView.setVisibility(View.VISIBLE);
    		mPasswordInputView.setText("");
    		
    		showPasswordErrorDialog();
    	}
    	else{
    		//Valid input
    		loginWithEmailAndPassword(email, password);
    	}
    	
    }
    
    private void signUpAction(){
    	Intent i = new Intent(this, SignupActivity.class);
		startActivity(i);
    }
    
    private String getEmail(){
    	return mEmailInputView.getText().toString();
    }
    
    private String getPassword(){
    	return mPasswordInputView.getText().toString();
    }
    
    
    //HTTP actions
    
    private void sendForgotPasswordEmail(String email){
    	//TODO
    }
    
    private void loginWithEmailAndPassword(String email, String password){
    	//TODO
    }
    
    
    
    //Dialogs
    private void showProgressDialog(){
    	mProgressDialog = ProgressDialog.show(this, "", "Loading");
    }
    
    private void hideProgressDialog(){
    	mProgressDialog.dismiss();
    }
    
    private void showPasswordErrorDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(LoginMainActivity.this);
		builder.setTitle("Invalid Password")
		.setMessage("Password must have at least 6 characters")
		.setPositiveButton("Ok", null)
		.setCancelable(true);
		AlertDialog alert = builder.create();
		alert.show();
    }
    
    
}
