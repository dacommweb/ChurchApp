package com.suntechnology.churchapp.helper;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.suntechnology.churchapp.inc.AppConfig;


import org.json.JSONException;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Flexy on 7/19/2017.
 */

 public  class  ApiRequest {
     Context context;
     ApiRequestComm apiRequestComm;
     Map<String, String> params;
     Boolean showLoader=false;

    public   ApiRequest(Context cont, HashMap<String, String> par, boolean loader){
        context=cont;
        params=par;
        showLoader=loader;
        apiRequestComm = (ApiRequestComm) cont;

            Global.loaderDialog=new ProgressDialog(context);


        request();
    }
    public   ApiRequest(Context cont,HashMap<String, String> para){
        this.context=cont;
        this.params=para;
        apiRequestComm = (ApiRequestComm) cont;
        if(Global.loaderDialog==null){
            Global.loaderDialog=new ProgressDialog(context);
        }
        request();
    }
    public interface ApiRequestComm{
        public void apiResponse(String response);
    }
    public void request() {
        String REQUEST_TAG = Context.class.getName();
        if(! Global.loaderDialog.isShowing() && showLoader){
            Global.showLoader(context,"") ;
        }

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.SERVER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if( Global.loaderDialog.isShowing()){
                     Global.loaderDialog.dismiss(); ;
                }
                try {
                    CodingMsg.l("success: " + response.toString());
                    apiRequestComm.apiResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  VolleyLog.d("DAV", "Error: " + error.getMessage());
                        try {
                            if (error != null) {
                                if (error.getMessage().contains("ConnectException")) {
                                    CodingMsg.t(context, "Check your internet connection");
                                }
                            }
                            CodingMsg.l("VolleyError: " + error.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                            CodingMsg.l("VolleyError: " + e.getMessage());
                        }

                        if( Global.loaderDialog.isShowing()){
                             Global.loaderDialog.dismiss(); ;
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding String request to request queue
        AppNetworkSingleton.getInstance(context.getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);
    }
}
