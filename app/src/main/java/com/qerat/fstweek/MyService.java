package com.qerat.fstweek;


import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.support.constraint.Constraints.TAG;

public class MyService extends FirebaseMessagingService {
    public MyService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        FirebaseUtilClass.getDatabaseReference().child("UserTokens").child(s).setValue(true);
    }

}
