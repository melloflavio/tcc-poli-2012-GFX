package com.poli.gfx.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesAdapter {
	
	//Shared Preferences
    public static final String SHARED_PREFERENCES_USERNAME_KEY = "user_name";
    public static final String SHARED_PREFERENCES_USER_ID_KEY = "user_id";
	
	
	public static String getStringFromSharedPreferences(String key, Context context){
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(key, "");
    }
	
	public static void storeStringToSharedPreferences(String key, String value, Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    	SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static void removeKeyFromSharedPreferences(String key, Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    	SharedPreferences.Editor editor = preferences.edit();
		editor.remove(key);
		editor.commit();
	}
	
	public static void clearSharedPreferences(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    	SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}
}

