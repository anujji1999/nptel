package com.nptel.courses.online;

import android.annotation.SuppressLint;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Utility {

    private static String userId;

    public static void setUserActivity(String event) {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("event", event);
        eventMap.put("time", Timestamp.now());

        Map<String, Object> record = new HashMap<>();
        record.put("flow", FieldValue.arrayUnion(eventMap));

        if (getUserId() == null)
            return;
        FirebaseFirestore.getInstance().document("production/production/users/" + userId + "/activity/uses")
                .set(record, SetOptions.merge());
    }

    public static String getUserId() {
        if (userId != null && userId.length() > 1)
            return userId;
        else {
            userId = MyApplication.ANDROID_ID;
            createUser();
            return userId;
        }
    }

    private static void createUser() {
        @SuppressLint("HardwareIds")
        Map<String, Object> user = new HashMap<>();
        user.put("uid", userId);
        user.put("isAnonymous", true);
        user.put("lastAccessed", FieldValue.serverTimestamp());
        user.put("androidId", MyApplication.ANDROID_ID);

        FirebaseFirestore.getInstance().collection("production/production/users")
                .document(userId)
                .set(user, SetOptions.merge());

    }
}
