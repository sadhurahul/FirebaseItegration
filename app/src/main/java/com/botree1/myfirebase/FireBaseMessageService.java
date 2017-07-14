package com.botree1.myfirebase;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

/**
 * Created by botree1 on 25/1/17.
 */

public class FireBaseMessageService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage == null) {
            Log.v("###", "remoteMessage is null");
        }

        if (remoteMessage.getNotification() != null) {
            Log.v("###", "remoteMessage :" + remoteMessage.getNotification().getTitle());
            handleNotification(remoteMessage);

        }

        if(remoteMessage.getData().size()>0){
            Log.v("###","remote Data"+remoteMessage.getData().toString());
        }
    }

    private void handleNotification(RemoteMessage remoteMessage) {

        if(!isAppIsInBackground(getApplicationContext())){

            Log.v("###","Notification Handle");
            NotificationCompat.Builder b=new NotificationCompat.Builder(this);
            b.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("Hearty365")
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                    .setContentInfo("Info");

            Intent notificationIntent=new Intent(this,MainActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            b.setContentIntent(pendingIntent);

            NotificationManager notificationManger=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManger.notify(0,b.build());

        } else{

        }

    }

    public boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcess = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo proceesInfo : runningProcess) {
                if (proceesInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeprocess : proceesInfo.pkgList) {
                        if (activeprocess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }

                }


            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
            ComponentName componentName = taskInfo.get(0).topActivity;
            if (componentName.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }

        }

        return isInBackground;


    }
}
