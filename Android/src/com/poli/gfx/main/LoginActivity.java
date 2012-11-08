package com.poli.gfx.main;

import org.apache.http.params.HttpAbstractParamBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.poli.gfx.R;
import com.poli.gfx.util.AppHttpClient;
import com.poli.gfx.util.Paths;
import com.poli.gfx.util.SharedPreferencesAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends Activity {

	private static final String TAG = LoginActivity.class.getSimpleName();
	
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
   	protected void onDestroy() {
   		if (isFinishing()) {
   			
   	        
   		}
   		
   		super.onDestroy();
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
    	
    	SharedPreferencesAdapter.storeStringToSharedPreferences(SharedPreferencesAdapter.USER_ID_KEY, "1", getApplicationContext());
    	SharedPreferencesAdapter.storeStringToSharedPreferences(SharedPreferencesAdapter.BASE_HOUSE_ID_KEY+0, "00", getApplicationContext());
    	SharedPreferencesAdapter.storeStringToSharedPreferences(SharedPreferencesAdapter.BASE_HOUSE_ID_KEY+1, "11", getApplicationContext());
    	SharedPreferencesAdapter.storeIntToSharedPreferences(SharedPreferencesAdapter.HOUSE_COUNT_KEY, 1, getApplicationContext());
		
		goToHomeScreen();
    }
    
    private void loginWithEmailAndPassword(String email, String password){
    	RequestParams params = new RequestParams();
    	params.put("email", email);
    	params.put("password", password);
    	
    	Log.d("BBB", "email = "+email+" pass = "+password+" params = "+params.toString());
    	
    	AppHttpClient.get(Paths.GET_ACCOUNT, params, new JsonHttpResponseHandler(){
//    	AppHttpClient.post(Paths.GET_ACCOUNT, params, new AsyncHttpResponseHandler(){
    		@Override
    		public void onStart() {
    	    	showProgressDialog();
    			Log.d(TAG, "Start");
    		}
    		
    		@Override
    		public void onSuccess(JSONArray response) {
    			Log.d(TAG, "Array Success = "+response.toString());
    		}
    		
    		@Override
    		public void onSuccess(JSONObject response) {
    			Log.d(TAG, "Success = "+response.toString());
    			
    			boolean success;
    			String userId;
    			JSONArray houses;
				try {
					success = response.getBoolean("status");
					userId = response.getString("userId");
					houses = response.getJSONArray("houses");
				} catch (JSONException e) {
					//TODO error receiving message from server
					success = false;//Default value
					userId = null;
					houses = null;
					e.printStackTrace();
				}
				if(success){
					Log.d(TAG, "true success");
					storeValuesToSharedPreferences(userId, houses);
					
					goToHomeScreen();
				}
				else{
					showLoginErrorDialog();
				}
    		}
    		
    		@Override
			public void onFailure(Throwable e) {
    			Log.d(TAG, "fail");
    			showLoginErrorDialog();
    		}
    		
    		
    		@Override
			public void onFailure(Throwable e, JSONObject response) {
    			showLoginErrorDialog();
    			Log.d(TAG, "Fail object  response = "+response.toString());
    		}
    		
    		@Override
			public void onFailure(Throwable e, String response) {
    			showLoginErrorDialog();
    			Log.d(TAG, "Fail object  response = "+response);
    		}
    		
    		@Override
			public void onFailure(Throwable e, JSONArray response) {
    			showLoginErrorDialog();
    			Log.d(TAG, "Fail array  response = "+response.toString());
    		}
    		
    		@Override
    		public void onFinish() {
    			hideProgressDialog();
    			Log.d(TAG, "Finish");
    		}
    	});
    }
    
    private void storeValuesToSharedPreferences(String userId, JSONArray houses){
    	SharedPreferencesAdapter.storeStringToSharedPreferences(SharedPreferencesAdapter.USER_ID_KEY, userId, getApplicationContext());
    	int length = houses.length();
    	for (int i = 0; i < length ; i ++){
    		JSONObject obj;
    		String currHouseId;
    		String currHouseName;
			try {
				obj = houses.getJSONObject(i);
				currHouseId = obj.getString("houseId");
				currHouseName = obj.getString("houseName");
			} catch (JSONException e) {
				currHouseId = null;
				currHouseName = null;
				e.printStackTrace();
			}
			if(currHouseId != null){
				SharedPreferencesAdapter.storeStringToSharedPreferences(SharedPreferencesAdapter.BASE_HOUSE_ID_KEY+i, currHouseId, getApplicationContext());
				SharedPreferencesAdapter.storeStringToSharedPreferences(SharedPreferencesAdapter.BASE_HOUSE_NAME_KEY+i, currHouseName, getApplicationContext());
			}
    	}
    	
    	if (length > 0){
    		SharedPreferencesAdapter.storeIntToSharedPreferences(SharedPreferencesAdapter.HOUSE_COUNT_KEY, length, getApplicationContext());
    	}
    }
    
    //Dialogs
    private void showProgressDialog(){
    	mProgressDialog = ProgressDialog.show(this, "", "Loading");
    }
    
    private void hideProgressDialog(){
    	mProgressDialog.dismiss();
    }
    
    private void showPasswordErrorDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
		builder.setTitle("Senha Inválida")
		.setMessage("A senha deve ter pelo menos 6 caracteres")
		.setPositiveButton("Ok", null)
		.setCancelable(true);
		AlertDialog alert = builder.create();
		alert.show();
    }
    
    private void showLoginErrorDialog(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
		builder.setTitle("Erro ao Logar")
		.setMessage("Usuário ou senha inválidos. Por favor verifique os dados e tente novamente.")
		.setPositiveButton("Ok", null)
		.setCancelable(true);
		AlertDialog alert = builder.create();
		alert.show();
    }
    
    
    //Flow to other screens
    
    private void goToHomeScreen(){
    	Intent i = new Intent(this, HomeScreenActivity.class);
		startActivity(i);
    }
}
