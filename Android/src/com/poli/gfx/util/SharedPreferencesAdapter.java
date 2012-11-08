package com.poli.gfx.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesAdapter {
	
	//Shared Preferences
    public static final String USER_ID_KEY = "user_id";
    public static final String BASE_HOUSE_ID_KEY = "house_id";
    public static final String BASE_HOUSE_NAME_KEY = "house_name";
    public static final String HOUSE_COUNT_KEY = "house_count";
	
	//String
	public static String getStringFromSharedPreferences(String key, Context context){
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(key, null);
    }
	
	public static void storeStringToSharedPreferences(String key, String value, Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    	SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	
	//Int
	public static int getIntFromSharedPreferences(String key, Context context){
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getInt(key, 0);
    }
	
	public static void storeIntToSharedPreferences(String key, int value, Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    	SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key, value);
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

