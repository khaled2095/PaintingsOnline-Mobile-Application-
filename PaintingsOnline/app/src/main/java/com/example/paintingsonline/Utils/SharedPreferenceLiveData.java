package com.example.paintingsonline.Utils;

import androidx.lifecycle.LiveData;
import android.content.SharedPreferences;

public abstract class SharedPreferenceLiveData<T> extends LiveData<T> {
    SharedPreferences sharedPreferences;
    String key;
    public T defVal;


    public SharedPreferenceLiveData(SharedPreferences sharedPreferences, String key, T defVal) {
        this.sharedPreferences = sharedPreferences;
        this.key = key;
        this.defVal = defVal;
    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (SharedPreferenceLiveData.this.key.equals(key)) {
                setValue(getValueFromPreferences(key, defVal));
            }
        }
    };

    abstract T getValueFromPreferences(String key, T defValue);


    @Override
    protected void onActive()
    {
        super.onActive();
        setValue(getValueFromPreferences(key, defVal));
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }


    @Override
    protected void onInactive()
    {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        super.onInactive();
    }

    public SharedPreferenceLiveData<Integer> getIntegerLiveData(String key, Integer defaultValue) {
        return new SharedPreferenceIntegerLiveData(sharedPreferences,key, defaultValue);
    }

    public SharedPreferenceLiveData<Boolean> getBooleanLiveData(String key, Boolean defaultValue) {
        return new SharedPreferenceBooleanLiveData(sharedPreferences,key, defaultValue);
    }
}


class SharedPreferenceStringLiveData extends SharedPreferenceLiveData<String>{

    public SharedPreferenceStringLiveData(SharedPreferences prefs, String key, String defValue) {
        super(prefs, key, defValue);
    }

    @Override
    String getValueFromPreferences(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }
}



