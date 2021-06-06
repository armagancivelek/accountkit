package com.armagancivelek.accountkit;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

public class MyPushService extends HmsMessageService {
    private static final String TAG = "deneme";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "onNewToken:" + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG, "onMessageReceived() "+ remoteMessage.getDataOfMap().get("title"));




        int icon = R.mipmap.ic_launcher;
        String title = remoteMessage.getDataOfMap().get("title");
        String text = remoteMessage.getDataOfMap().get("text");
        String channelId = remoteMessage.getDataOfMap().get("channel_id");

        if(channelId == null){
            channelId = Constant.NotificationChannel2.ID;
        }
        if(!channelId.equals(Constant.NotificationChannel1.ID)){
            channelId = Constant.NotificationChannel2.ID;
        }

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Notification notification = new NotificationCompat
                .Builder(this, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setColor(this.getResources().getColor(R.color.colorPrimary))
                .build();

        notificationManager.notify(1, notification);
    }


}
