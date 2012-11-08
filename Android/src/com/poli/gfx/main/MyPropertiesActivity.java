package com.poli.gfx.main;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.poli.gfx.R;
import com.poli.gfx.model.Residencia;
import com.poli.gfx.util.AppHttpClient;
import com.poli.gfx.util.Paths;
import com.poli.gfx.util.SharedPreferencesAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MyPropertiesActivity extends Activity implements OnItemSelectedListener{

private static final String TAG = MyPropertiesActivity.class.getSimpleName();

private ArrayList<String> mHouseNames;//Usado para o spinner adapter
private ArrayList<String> mHouseIds;//Usado para os requests

private ArrayList<Residencia> mResidencias;

private ProgressDialog mProgressDialog;

private Spinner mSpinner;
private ArrayAdapter<String> mSpinnerAdapter;
	
	//Activity lifecycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_properties);
        
        ((TextView) findViewById(R.id.header_title)).setText("Minhas Casas");
        
        
        mHouseNames = new ArrayList<String>();
        mHouseIds = new ArrayList<String>();
        mResidencias = new ArrayList<Residencia>();
        
        getHousesFromSharedPrefs();
        
        requestAllPropertiesInfo();
        
        mSpinner = (Spinner) findViewById(R.id.myproperties_spinner);
        mSpinnerAdapter = getSimpleAdapter(MyPropertiesActivity.this, mHouseNames);
        mSpinner.setAdapter(mSpinnerAdapter);
        mSpinner.setOnItemSelectedListener(this);
        
    }
    
    @Override
   	protected void onDestroy() {
   		if (isFinishing()) {
   			   		   	      
   		}
   		
   		super.onDestroy();
   	}
    
    private void getHousesFromSharedPrefs(){
    	int houseCount = SharedPreferencesAdapter.getIntFromSharedPreferences(SharedPreferencesAdapter.HOUSE_COUNT_KEY, getApplicationContext());
        
    	String houseName;
    	String houseId;
    	
        for (int i = 0 ; i < houseCount ; i++){
        	houseName = SharedPreferencesAdapter.getStringFromSharedPreferences(SharedPreferencesAdapter.BASE_HOUSE_NAME_KEY+i, getApplicationContext());
        	houseId= SharedPreferencesAdapter.getStringFromSharedPreferences(SharedPreferencesAdapter.BASE_HOUSE_ID_KEY+i, getApplicationContext());
        	
        	if(houseName != null && houseId != null){
        		Log.d(TAG, "house found! id = "+houseId+" name = "+houseName);
        		mHouseIds.add(houseId);
//        		mHouseNames.add(houseName);
        	}
        }
        
    }
    
    private void requestAllPropertiesInfo(){
    	for (String houseId : mHouseIds){
    		requestPropertyInfo(houseId);
    	}
    }
    
    private void requestPropertyInfo(final String houseId) {
    	RequestParams params = new RequestParams();
    	params.put("houseId", houseId);
    	
    	Log.d(TAG, "houseId = "+houseId+"params = "+params.toString());
    	
    	AppHttpClient.get(Paths.GET_HOUSE_INFO, params, new JsonHttpResponseHandler(){
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
    			JSONObject houseInfo;
				try {
					success = response.getBoolean("status");
					houseInfo = response.getJSONObject("casa");
				} catch (JSONException e) {
					//TODO error receiving message from server
					success = false;//Default value
					houseInfo = null;
					e.printStackTrace();
				}
				if(success){
					Log.d(TAG, "true success");
					parseHouseInfo(houseInfo, houseId);
					
				}
				else{
					showHouseRequestErrorDialog();
				}
    		}
    		
    		@Override
			public void onFailure(Throwable e) {
    			Log.d(TAG, "fail");
    			showHouseRequestErrorDialog();
    		}
    		
    		
    		@Override
			public void onFailure(Throwable e, JSONObject response) {
    			showHouseRequestErrorDialog();
    			Log.d(TAG, "Fail object  response = "+response.toString());
    		}
    		
    		@Override
			public void onFailure(Throwable e, String response) {
    			showHouseRequestErrorDialog();
    			Log.d(TAG, "Fail object  response = "+response);
    		}
    		
    		@Override
			public void onFailure(Throwable e, JSONArray response) {
    			showHouseRequestErrorDialog();
    			Log.d(TAG, "Fail array  response = "+response.toString());
    		}
    		
    		@Override
    		public void onFinish() {
    			hideProgressDialog();
    			Log.d(TAG, "Finish");
    		}
    	});
    }
    
    private ArrayAdapter<String> getSimpleAdapter(Context context, ArrayList<String> list){
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list){
		};
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		return adapter;
    }

	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		
		updateViewHouseInfo(pos);
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	///Dialogs
	
	private void showHouseRequestErrorDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MyPropertiesActivity.this);
		builder.setTitle("Erro de Conexao")
		.setMessage("Erro ao receber informações da residência.")
		.setPositiveButton("Ok", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				goToHomeScreen();
			}
		})
		.setCancelable(true);
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	//Dialogs
    private void showProgressDialog(){
    	if(mProgressDialog != null && mProgressDialog.isShowing())
    	{
    		mProgressDialog.dismiss();
    	}
    	mProgressDialog = ProgressDialog.show(this, "", "Carregando Residências");
    }
    
    private void hideProgressDialog(){
    	Log.d(TAG, "Hiding Progress Dialog");
    	mProgressDialog.dismiss();
    }
    
    private void parseHouseInfo(JSONObject houseInfo, String houseId) {
    	Residencia r = new Residencia();
    	String nomeCasa;
    	String logradouro;
    	String cidade;
    	String estado;
    	
    	try {
			nomeCasa = houseInfo.getString("nomeCasa");
			logradouro = houseInfo.getString("logradouro");
	    	cidade = houseInfo.getString("cidade");
	    	estado = houseInfo.getString("estado");
		} catch (JSONException e) {
			nomeCasa = null;
			logradouro = null;
	    	cidade = null;
	    	estado = null;
			e.printStackTrace();
		}
    	
    	if (nomeCasa != null){
    		r.setNomeCasa(nomeCasa);
    		r.setLogradouro(logradouro);
    		r.setCidade(cidade);
    		r.setEstado(estado);
    		r.setHouseId(houseId);
    		
    		mResidencias.add(r);
    		mHouseNames.add(nomeCasa);
    		
    		mSpinnerAdapter.notifyDataSetChanged();
    	}
    }
    
    private void updateViewHouseInfo(int index){
    	Residencia r = mResidencias.get(index);
    	((TextView)findViewById(R.id.myProperties_houseName)).setText(r.getNomeCasa());
    	((TextView)findViewById(R.id.myProperties_houseAddr)).setText(r.getLogradouro());
    	((TextView)findViewById(R.id.myProperties_houseCity)).setText(r.getCidade());
    	((TextView)findViewById(R.id.myProperties_houseState)).setText(r.getEstado());
    }
    
    private void goToHomeScreen(){
    	Intent i = new Intent(this, HomeScreenActivity.class);
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
    }
}
