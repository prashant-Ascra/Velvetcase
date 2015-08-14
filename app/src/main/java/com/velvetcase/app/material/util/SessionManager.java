package com.velvetcase.app.material.util;

import java.util.HashMap;

import com.velvetcase.app.material.MainActivity;
import com.velvetcase.app.material.SignActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;
	// Editor for Shared preferences
	Editor editor;
	// Context
	Context _context;
	// Shared pref mode
	int PRIVATE_MODE = 0;
	// Sharedpref file name
	public static final String PREF_NAME = "VelvetcasePref";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
	private static final String IS_LIKE = "IsLike";
    private static final String IS_FIRST_TIME = "firsttime";

	public static final String Email = "Email";
	public static final String SessionToken = "SessionToken";
	public static final String Uid = "user_id";
	public static final String FullName = "full_name";
	public static final String secret_key = "secret_key";
	public static final String key = "key";
	public static final String authentication_token = "authentication_token";
	public static final String CellPhone = "CellPhone";
	public static final String SHARE_URL = "url";

    private static final String IS_DESIGNER= "IsDesigner";
    private static final String IS_SINGLEPRODUCT= "IsSingleproduct";
    public static final String Heart = "heart";
    public static final String OCCASION_NAME = "occasion";

	// Constructor
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	public void createLoginSession(String email ,String uid,
			String full_name) {
			editor.putBoolean(IS_LOGIN, true);
			editor.putString(Email, email);
			editor.putString(Uid, uid);
			editor.putString(FullName, full_name);
		// commit changes
			editor.commit();
	}


	/**
	 * Check login method wil check user login status If false it will redirect
	 * user to login page Else won't do anything
	 * */
	public void checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, MainActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// Staring Login Activity
			_context.startActivity(i);
		}

	}




	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(Email, pref.getString(Email, null));
		user.put(Uid, pref.getString(Uid, null));
		user.put(FullName, pref.getString(FullName, null));
		return user;
	}

public  void cleanUID(){
	editor.putString(Uid,null);
}
    public  boolean isITFirstTime(){

        return pref.getBoolean(IS_FIRST_TIME, false);

    }

    public  void  set_first_Time(Boolean flag) {
		editor.putBoolean(IS_FIRST_TIME,flag);
        editor.commit();
    }


	public  void  set_share_url(String url_link){
		editor.putString(SHARE_URL, url_link);
		editor.commit();
	}

	public String get_share_url(){
		return pref.getString(SHARE_URL, null);
	}

public  void set_occasion_name(String occasion_name){
editor.putString(OCCASION_NAME,occasion_name);
 editor.commit();
}

   public String get_occasion_name(){
       return pref.getString(OCCASION_NAME, null);
   }

	public void logoutUser(String user_id) {
		try{
		// Clearing all data from Shared Preferences
		editor = pref.edit();
		editor.clear();
		editor.commit();
			editor.putString(Uid,user_id);
            editor.putBoolean(IS_FIRST_TIME,true);
            editor.commit();
		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, SignActivity.class);
		// Closing all the Activities
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		_context.startActivity(i);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn() {

        return pref.getBoolean(IS_LOGIN, false);
	}


    public String getUserID() {
        return pref.getString(Uid, null);
    }

    public void setDesigner(Boolean flag) {
        editor.putBoolean(IS_DESIGNER, flag);
        editor.commit();
    }
    public boolean isDesigner() {
        return pref.getBoolean(IS_DESIGNER, false);
    }

    public void setSingleProduct(Boolean flag) {
        editor.putBoolean(IS_SINGLEPRODUCT, flag);
        editor.commit();
    }
    public boolean isSingleProduct() {
        return pref.getBoolean(IS_SINGLEPRODUCT, false);
    }


    public  void setHeartSelected(boolean heart_val){
        editor.putBoolean(Heart, heart_val);
        editor.commit();
    }
    public boolean isHeartSelected(){
        return pref.getBoolean(Heart, false);
    }

}
