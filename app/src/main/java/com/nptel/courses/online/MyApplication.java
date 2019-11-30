package com.nptel.courses.online;

import android.annotation.SuppressLint;
import android.app.Application;
import android.preference.PreferenceManager;
import android.provider.Settings;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.nptel.courses.online.entities.User;

import java.util.HashMap;
import java.util.Map;


public class MyApplication extends Application {

    public static String ANDROID_ID;
    public static String courseId;
    public static String courseName;
    public static String playlistId;
    public static User user;

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate() {
        super.onCreate();
        ANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        courseId = PreferenceManager.getDefaultSharedPreferences(this).getString("courseID", "00");
        courseName = PreferenceManager.getDefaultSharedPreferences(this).getString("courseName", "00");
        Utility.setUserActivity("Application Started");
        Map<String, Object> lastAccessed = new HashMap<>();
        lastAccessed.put("lastAccessed", FieldValue.serverTimestamp());
        FirebaseFirestore.getInstance().document("production/production/users/" + Utility.getUserId())
                .set(lastAccessed, SetOptions.merge());

    }
}
