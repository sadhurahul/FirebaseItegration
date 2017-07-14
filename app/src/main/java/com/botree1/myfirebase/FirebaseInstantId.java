package com.botree1.myfirebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by botree1 on 25/1/17.
 */

public class FirebaseInstantId extends FirebaseInstanceIdService {

    @Override
    public void onCreate() {
        super.onCreate();


        Log.v("###","FirebaseinstannId");
    }


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Log.v("###","onToakenRefresh()");

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        storeRegIdPref(refreshedToken);
        Log.v("###",refreshedToken);

        Intent registrationComplete = new Intent("Registration Complate");
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void storeRegIdPref(String refreshedToken) {
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("storeRegIdPref",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("regId",refreshedToken);
        editor.commit();
    }

}
