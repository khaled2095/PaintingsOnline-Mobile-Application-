package com.example.paintingsonline.Utils;

import android.content.SharedPreferences;

public class SharedPreferenceBooleanLiveData extends SharedPreferenceLiveData<Boolean>{

    public SharedPreferenceBooleanLiveData(SharedPreferences prefs, String key, Boolean defValue) {
        super(prefs, key, defValue);
    }

    @Override
    Boolean getValueFromPreferences(String key, Boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }
}
