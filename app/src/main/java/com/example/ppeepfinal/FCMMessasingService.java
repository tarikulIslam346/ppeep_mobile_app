package com.example.ppeepfinal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.ppeepfinal.data.OrderMerchantModel;
import com.example.ppeepfinal.data.UserDatabase;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMMessasingService extends FirebaseMessagingService {
    private static final String TAG = "FCMMessasingService";

   // public String message;
    UserDatabase mdb;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        /*mdb = UserDatabase.getInstance(getApplicationContext());
        OrderMerchantModel orderMerchantModel = new OrderMerchantModel(7,"AR fantasia",40,100);
        mdb.orderMercahntDAO().insertOrderMerchant(orderMerchantModel);*/
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
               // handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //message = remoteMessage.getNotification().getBody();
            Intent it = new Intent(this, HomePage.class);
           // Snackbar.make(R.id.layout_home_page),"Order_has",Snackbar.LENGTH_INDEFINITE).show();
            PendingIntent contentIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), it, 0);
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            int ico_notification = R.drawable.ic_ajkerdeal;
            int color = ContextCompat.getColor(this, R.color.colorAccent);

            NotificationManager mNotificationManager = (NotificationManager)
                    this.getSystemService(Context.NOTIFICATION_SERVICE);

            String CHANNEL_ID = "FCM_channel_007";
            CharSequence name = "Channel MyChannel";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mNotificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this,CHANNEL_ID)
                            .setSmallIcon(ico_notification)
                            .setContentTitle(getString(R.string.app_name))
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(remoteMessage.getNotification().getBody()))
                            .setSound(soundUri)
                            .setColor(color)
                            .setAutoCancel(true)
                            .setVibrate(new long[]{1000, 1000})
                            .setContentText(remoteMessage.getNotification().getBody());

            mBuilder.setContentIntent(contentIntent);
            Notification notification = mBuilder.build();

            mNotificationManager.notify(0, notification);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
