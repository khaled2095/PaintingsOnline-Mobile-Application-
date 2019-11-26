package com.example.paintingsonline.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager
{
    private static SharedPrefManager instance;
    private static Context ctx;

    private SharedPreferenceBooleanLiveData spbool;
    private SharedPreferenceStringLiveData spString;
    private SharedPreferenceIntegerLiveData spInt;

    private String SHARED_PREF_NAME = "my1";
    private String KEY_USER = "usertype";
    private String KEY_VERIFIED = "verify";
    private String KEY_FULLNAME = "fullname";
    private String KEY_PASSWORD = "pass";
    private String KEY_ADDRESS = "address";
    private String KEY_USERNAME = "username";
    private String KEY_EMAIL = "email";
    private String KEY_ID = "id";




    private SharedPrefManager(Context context) {
        ctx = context;

    }

    public SharedPreferenceBooleanLiveData getSpbool() {
        return spbool;
    }

    public SharedPreferenceStringLiveData getSpString() {
        return spString;
    }

    public SharedPreferenceIntegerLiveData getSpInt() {
        return spInt;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void userlogin(int id, String username, String password ,String email, String address, String fullname, int usertype, int verify)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_FULLNAME, fullname);
        editor.putString(KEY_ADDRESS, address);
        editor.putInt(KEY_USER, usertype);
        editor.putInt(KEY_VERIFIED, verify);
        editor.apply();

        spInt = new SharedPreferenceIntegerLiveData(sharedPreferences, KEY_VERIFIED, verify);
    }

    public boolean isLoggedIn()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USERNAME, null) != null)
        {
            return true;
        }

        return false;
    }

    public void logout()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void SetVerify(int Verify)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_VERIFIED, Verify);
        editor.apply();
    }

    public int getUserID()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, 0);
    }

    public int getUserType()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER, 0);
    }

    public int getVerifiedUser()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_VERIFIED, 0);
    }


    //This method only stores User name from facebook to use it as a USERNAME for login process
    public void SetUserName(String FBUserName)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, FBUserName);
        editor.apply();
    }


    public String getUserName()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null);
    }


    //This method only stores User email from facebook to use it as a USERNAME for login process
    public void SetUserEmail(String FBUserEmail)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, FBUserEmail);
        editor.apply();
    }


    public String getUserEmail()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null);
    }


    public String getUserAddress()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ADDRESS, null);
    }

    public String getFullName()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FULLNAME, null);
    }

    public String password()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PASSWORD, null);
    }
}
