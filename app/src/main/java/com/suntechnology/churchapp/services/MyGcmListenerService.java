package com.suntechnology.churchapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.google.android.gms.gcm.GcmListenerService;
import com.suntechnology.churchapp.MainActivity;
import com.suntechnology.churchapp.R;
import com.suntechnology.churchapp.helper.CodingMsg;


import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by FRED on 3/15/2017.
 */

public class MyGcmListenerService extends GcmListenerService {
    private String contentstr="";
    private String contentimg="";
    private String chatwithId="";
    private String chatwith="";
    private String chatwithplayerId="";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */

    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data){
        //lighting the phone if its off
        PowerManager pm = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        // Check if message contains a notification payload.
        if(!isScreenOn)
        {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,"MyLock");
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");
            wl.acquire(1000);
            wl_cpu.acquire(1000);
        }
        CodingMsg.l("onMessageReceived:"+data.toString());
        try {
            JSONObject json= new JSONObject(data.getString("custom"));
            JSONObject json1= new JSONObject(json.getString("a"));
            contentstr=json1.getString("message");
            chatwithId = json1.getString("chatwithId");
            chatwith = json1.getString("chatwith");
            chatwithplayerId = json1.getString("chatwithplayerId");
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("chatwithId",chatwithId);
            intent.putExtra("chatwith",chatwith);
            intent.putExtra("chatwithplayerId",chatwithplayerId);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            if(!GlobalVariable.player_ids_chatting.contentEquals(chatwithplayerId) || GlobalVariable.currentlyChatting.contentEquals("0")){
//               if( GlobalVariablelobalVariable.getCurrentUser().getNotificationStatus().contains("1")){
                   showBigNotification(contentimg,"Dante Notification","From "+chatwith+": "+contentstr,intent);
//               }
//
//            }
        }catch (Exception e) {
            e.printStackTrace();
            CodingMsg.l("onMessageReceived:"+data.toString());
        }
    }
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void showBigNotification(String imgUrl, String title, String message, Intent intent) {

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
         PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(getBitmapFromURL(imgUrl));
        Notification notification;
        notification = mBuilder.setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
               // .setSound()
                .setStyle(bigPictureStyle)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo)
              //  .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.logo))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(20000), notification);
    }


    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
   /* private void sendNotification(String title,String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("chatWith",chatWith);
        intent.putExtra("chatWithId", chatwithId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new Notification();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setTicker("Taxy Deals")
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        notification.defaults |= Notification.DEFAULT_SOUND;

        notification.defaults |= Notification.DEFAULT_VIBRATE;


        NotificationManager notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(20000), notificationBuilder.build());
    }*/
}
