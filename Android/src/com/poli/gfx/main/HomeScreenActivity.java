package com.poli.gfx.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.poli.gfx.R;
import com.poli.gfx.util.SharedPreferencesAdapter;

public class HomeScreenActivity extends Activity {

	private static final String TAG = HomeScreenActivity.class.getSimpleName();
	
	//Activity lifecycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        ((TextView) findViewById(R.id.header_title)).setText("GFX");
        
        
    }
    
    @Override
   	protected void onDestroy() {
   		if (isFinishing()) {
   			   		   	      
   		}
   		
   		super.onDestroy();
   	}
    
    
    public void minhasCasasButtonClicked(View v){
    	Intent i = new Intent(this, MyPropertiesActivity.class);
		startActivity(i);
    }
    
    public void sobreButtonClicked(View v){
    	Intent i = new Intent(this, AboutActivity.class);
		startActivity(i);
    }
    
    public void medidasButtonClicked(View v){
    	Intent i = new Intent(this, ConsumoActivity.class);
    	startActivity(i);
    }
    
    public void sairButtonClicked(View v){
    	SharedPreferencesAdapter.clearSharedPreferences(getApplicationContext());
    	Intent i = new Intent(this, MainActivity.class);
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(i);
    }

}
