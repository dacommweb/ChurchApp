package com.suntechnology.churchapp.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

import java.math.BigInteger;

/**
 * Created by FRED on 6/6/2017.
 */
public class MyNotificationExtenderService extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
//        OverrideSettings overrideSettings = new OverrideSettings();
//        overrideSettings.extender = new NotificationCompat.Extender() {
//            @Override
//            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
//                // Sets the background notification color to Red on Android 5.0+ devices.
//                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
//                        R.drawable.logo);
//                builder.setLargeIcon(icon);
//                return builder.setColor(new BigInteger("FF0000FF", 16).intValue());
//            }
//        };
//
//        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
//        Log.d("Fred", "Notification displayed with id: " + displayedResult.androidNotificationId);

        return true;
    }
}
