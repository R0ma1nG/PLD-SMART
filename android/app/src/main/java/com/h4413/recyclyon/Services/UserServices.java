package com.h4413.recyclyon.Services;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.h4413.recyclyon.Model.User;
import com.h4413.recyclyon.Utilities.SharedPreferencesKeys;

public class UserServices {
    public static User getCurrentUserFromSharedPreferences(AppCompatActivity activity) {
        Gson gson = new Gson();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        String str = sharedPref.getString(SharedPreferencesKeys.USER_KEY, "");
        if(str == null) return null;
        return gson.fromJson(str, User.class);
    }
}
