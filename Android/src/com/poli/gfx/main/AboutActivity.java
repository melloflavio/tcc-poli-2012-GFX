package com.poli.gfx.main;

import com.poli.gfx.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {

	
	private static final String TAG = AboutActivity.class.getSimpleName();
	
	//Activity lifecycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        
        ((TextView) findViewById(R.id.header_title)).setText("Sobre");
        
        
    }
    
    @Override
   	protected void onDestroy() {
   		if (isFinishing()) {
   			   		   	      
   		}
   		
   		super.onDestroy();
   	}
}
