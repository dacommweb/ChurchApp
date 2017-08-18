package com.suntechnology.churchapp.inc;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.firebase.client.Firebase;
import com.onesignal.OneSignal;
import com.suntechnology.churchapp.services.MyNotificationOpenedHandler;
import com.suntechnology.churchapp.services.MyNotificationReceivedHandler;

/**
 * Created by Flexy on 8/11/2016.
 */
public class AppConfig extends MultiDexApplication {
    public static final String SERVER_URL = "http://electronicbeauty.com/projects/churchapp/controller.php";
    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.None)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                .setNotificationReceivedHandler( new MyNotificationReceivedHandler())
                .init();
        Firebase.setAndroidContext(this);
    }

}
