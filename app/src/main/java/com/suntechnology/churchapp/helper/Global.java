package com.suntechnology.churchapp.helper;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.NavigationView;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import com.suntechnology.churchapp.R;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by FRED on 7/12/2017.
 */

public class Global extends Application{
    public static final boolean SHOWLOGS = true;
    public static Dialog loaderDialog;
    public static void showLoader(Context context,String message) {
        loaderDialog = new Dialog(context);
        loaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loaderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loaderDialog.setContentView(R.layout.loading_layout);

        TextView txtMessage = (TextView) loaderDialog.findViewById(R.id.txtMessage);
        txtMessage.setText(message);
        loaderDialog.show();
    }
    public static boolean isDataConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null)
            return false;

        return connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void saveToPreference(Context context, String preferenceName, String preferenceValue, String fileName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String getFromPreferences(Context context, String preferenceName, String fileName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, "");
    }
    public static void alertDialog(Context context,String title, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogue_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);
        txtTitle.setText(title);
        txtMessage.setText(Html.fromHtml(message));
        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    public static void goToActivity(Context context, Class  className, HashMap<String,String> intentData) {
        Intent intent = new Intent(context, className);
        for (Map.Entry<String, String> data : intentData.entrySet()) {
            intent.putExtra(data.getKey(),data.getValue());
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }
    public static String displayTimestamp(String strTimestamp){
        // Date date = new Date(strTimestamp);
        Timestamp stamp = new Timestamp(Long.parseLong(strTimestamp));
        // Date date = new Date(stamp.getTime());
        Date date = new Date(Long.parseLong(strTimestamp)*1);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        return  formatter.format(date);
    }

    public static void CheckMenus(Menu menu, Context context, NavigationView navigationView){

       //  MenuItem nav_login = menu.findItem(R.id.nav_login);

    }

    public static void MenusView(int id,Context context,Activity activity) {
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

    }
}
