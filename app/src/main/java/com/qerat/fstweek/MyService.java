package com.qerat.fstweek;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.support.constraint.Constraints.TAG;

public class MyService extends FirebaseMessagingService {
    public static final String PREFERENCE_NAME = "MyPreferenceFileName";
    public MyService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        SharedPreferences pref = getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("UserToken", s);
        editor.commit();

    }



}
