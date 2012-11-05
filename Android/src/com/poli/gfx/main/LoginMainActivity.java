package com.poli.gfx.main;

import org.apache.http.params.HttpAbstractParamBean;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.poli.gfx.R;
import com.poli.gfx.util.AppHttpClient;
import com.poli.gfx.util.Paths;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class LoginMainActivity extends Activity {

	private static final String TAG = LoginMainActivity.class.getSimpleName();
	
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
    		mEmailErrorView.setVisibility(View.GONE);
    		mPasswordErrorView.setVisibility(View.GONE);
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
    	RequestParams params = new RequestParams();
    	params.put("email", email);
    	params.put("password", password);
    	
    	Log.d("BBB", "Loggin in");
    	
    	AppHttpClient.post(Paths.GET_ACCOUNT, params, new JsonHttpResponseHandler(){
//    	AppHttpClient.post(Paths.GET_ACCOUNT, params, new AsyncHttpResponseHandler(){
    		@Override
    		public void onStart() {
    	    	showProgressDialog();
    			Log.d(TAG, "Start");
    		}
    		
    		@Override
    		public void onSuccess(JSONObject response) {
    			Log.d(TAG, "Success = "+response.toString());
    			
    			boolean success;
				try {
					success = response.getBoolean("status");
				} catch (JSONException e) {
					//TODO error receiving message from server
					success = false;//Default value
					e.printStackTrace();
				}
				if(success){
					Log.d(TAG, "Oh YEAH!");
				}
				else{
					Log.d(TAG, "NOOOOOOOOO");
				}
    		}
    		
    		@Override
			public void onFailure(Throwable e) {
    			Log.d(TAG, "Fail");
    		}
    		
    		@Override
			public void onFailure(Throwable e, String response) {
    			Log.d(TAG, "Fail String  response = "+response);
    		}
    		
    		@Override
    		public void onFinish() {
    			hideProgressDialog();
    			Log.d(TAG, "Finish");
    		}
    	});
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
