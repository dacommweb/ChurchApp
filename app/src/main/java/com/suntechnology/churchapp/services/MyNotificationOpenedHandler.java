package com.suntechnology.churchapp.services;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.suntechnology.churchapp.helper.CodingMsg;

import org.json.JSONObject;

/**
 * Created by FRED on 6/6/2017.
 */
public class MyNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.
    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;

        CodingMsg.l("onMessageresultd:"+result.toString());
        //While sending a Push notification from OneSignal dashboard
        // you can send an addtional data named "activityToBeOpened" and retrieve the value of it and do necessary operation
        //If key is "activityToBeOpened" and value is "AnotherActivity", then when a user clicks
        //on the notification, AnotherActivity will be opened.
        //Else, if we have not set any additional data MainActivity is opened.
        if (data != null) {
            CodingMsg.l("onMessageReceived:"+data.toString());
            //activityToBeOpened = data.optString("activityToBeOpened", null);


        }

        //If we send notification with action buttons we need to specidy the button id's and retrieve it to
        //do the necessary operation.
        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
            CodingMsg.l("Button pressed with id: " + result.action.actionID);

        }
    }
}
