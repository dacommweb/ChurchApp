package com.suntechnology.churchapp.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;



/**
 * Created by Flexy on 9/19/2015.
 */
public class CodingMsg {

    public static void l(String message){
        if(Global.SHOWLOGS){
            Log.d("DAV", message);
        }
    }
    public static void t(Context con, String msg){
        Toast.makeText(con, msg, Toast.LENGTH_SHORT).show();
    }
}
