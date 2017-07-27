package com.suntechnology.churchapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntechnology.churchapp.classes.Devotional;
import com.suntechnology.churchapp.classes.Events;
import com.suntechnology.churchapp.classes.MapWrapper;
import com.suntechnology.churchapp.helper.ApiRequest;
import com.suntechnology.churchapp.helper.CodingMsg;
import com.suntechnology.churchapp.helper.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by FRED on 7/14/2017.
 */

public class DailyDevotional extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,ApiRequest.ApiRequestComm, View.OnClickListener {

    private NavigationView navigationView;
    private Menu menu;
    private HashMap<String,Devotional> devotionalList=new HashMap<>();
    private Toolbar toolbar;
    private TextView txtNextDevotional,txtCurrentDevotional,txtPrevDevotional,txtTopic,txtVerse,txtContent;
    private SimpleDateFormat devotionalDateFormat  = new SimpleDateFormat("MMMM dd yyyy");
    private String currentDate="";
    private Devotional currentDevotional=new Devotional();
    Calendar c = Calendar.getInstance();
    private int devotionalSize=0;
    Gson gson = new Gson();
    private Dialog devotionalDialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailydevotional);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        initToolbar();

        CodingMsg.l("oncreate called");
        HashMap<String, String> par=new HashMap<>();
        par.put("action","get_devotional");
        par.put("timestamp",TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "");
       // new ApiRequest(this,par,true);

    }
    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        menu = navigationView.getMenu();

        Global.CheckMenus(menu,this,navigationView);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void init() {
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtNextDevotional = (TextView) findViewById(R.id.txtNextDevotional);
        txtCurrentDevotional = (TextView) findViewById(R.id.txtCurrentDevotional);
        txtPrevDevotional = (TextView) findViewById(R.id.txtPrevDevotional);
        txtTopic = (TextView) findViewById(R.id.txtTopic);
        txtVerse = (TextView) findViewById(R.id.txtVerse);
        txtContent = (TextView) findViewById(R.id.txtContent);
        txtPrevDevotional.setOnClickListener(this);
        txtCurrentDevotional.setOnClickListener(this);
        txtNextDevotional.setOnClickListener(this);

    }
    private void populateData(String dateStr) {
        if(dateStr.contentEquals("")){
            dateStr = devotionalDateFormat.format(new Date());


            try {
                txtCurrentDevotional.setText(dateStr);
                c.setTime(devotionalDateFormat.parse(dateStr));
                c.add(Calendar.DATE, -1);
                txtPrevDevotional.setText(devotionalDateFormat.format(c.getTimeInMillis()));
                c.add(Calendar.DATE, 2);
                txtNextDevotional.setText(devotionalDateFormat.format(c.getTimeInMillis()));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        currentDate=dateStr;
       if( devotionalList.containsKey(dateStr)){

           //CodingMsg.l("currentDate:"+dateStr);

           currentDevotional=devotionalList.get(dateStr);
           txtTopic.setText(currentDevotional.getTitle());
           txtVerse.setText(currentDevotional.getVerse());
           txtContent.setText(currentDevotional.getContent());
       }else{
           txtTopic.setText("");
           txtVerse.setText("");
           txtContent.setText("");
           HashMap<String, String> par=new HashMap<>();
           try {
             Date date=  devotionalDateFormat.parse(dateStr);
               CodingMsg.l("tike "+date.getTime());
               CodingMsg.l(" mili "+TimeUnit.MILLISECONDS.toSeconds(date.getTime()));
               par.put("date",TimeUnit.MILLISECONDS.toSeconds(date.getTime()) + "");
           } catch (ParseException e) {
               e.printStackTrace();
           }
           par.put("timestamp",TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "");
           par.put("action","get_devotional");
           par.put("requestedDate",dateStr);

           new ApiRequest(this,par,true);
       }
    }
    private void showDevotionalDialogue() {
        devotionalDialogue = new Dialog(this);
        devotionalDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);
        devotionalDialogue.setContentView(R.layout.add_devotional_layout);
        devotionalDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imgBack = (ImageView) devotionalDialogue.findViewById(R.id.imgBack);
        RecyclerView alertRV = (RecyclerView) devotionalDialogue.findViewById(R.id.alertRV);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devotionalDialogue.dismiss();
            }
        });
        devotionalDialogue.setCancelable(true);
        devotionalDialogue.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CodingMsg.l("onresume called");
        if(devotionalList.size()<0){
            String wrapperStr=Global.getFromPreferences(this,"devotionalList","devotional");
            MapWrapper wrapper = gson.fromJson(wrapperStr, MapWrapper.class);
            devotionalList = wrapper.getMyMap();
            devotionalSize=devotionalList.size();
        }

        populateData(currentDate);
    }

    @Override
    protected void onPause() {
        super.onPause();
        CodingMsg.l("onPause called");
        //saving hasmap to preference
        if(devotionalSize<devotionalList.size()){
            MapWrapper wrapper = new MapWrapper();
            wrapper.setMyMap(devotionalList);
            String serializedMap = gson.toJson(wrapper);
            Global.saveToPreference(this,"devotionalList",serializedMap,"devotional");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Global.MenusView(id,this,DailyDevotional.this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void apiResponse(String response) {
        if (response == null) {
            if (Global.isDataConnectionAvailable(this)){
                CodingMsg.t(this,"Internet connection lost.");
            }
        } else {
            try {

                JSONObject jSon = new JSONObject(response);
                if (jSon.getString("status").equals("success")) {
                    if (jSon.getString("action").equals("get_devotional")) {

                        JSONArray jSone = new JSONArray(jSon.getString("devotionals"));
                        CodingMsg.l("JSONArray: "+jSone.length());
                        Devotional devotional;
                        for (int i = 0; i < jSone.length(); i++) {
                           // CodingMsg.l("JSONArray: "+i);
                            devotional = new Devotional();
                            jSon = jSone.getJSONObject(i);
                            //CodingMsg.l("devotional list"+jSon.toString());
                            devotional.setTitle(jSon.getString("devotional_title"));
                            devotional.setVerse(jSon.getString("devotional_verse"));
                            devotional.setContent(jSon.getString("devotional_content"));
                            devotional.setDate(jSon.getString("devotional_date"));
                            devotional.setHdate(jSon.getString("devotional_hdate"));
                            String srt= devotionalDateFormat.format(Double.parseDouble(jSon.getString("devotional_date")+"000"));
                            CodingMsg.l("devotional_title: "+jSon.getString("devotional_title")+"  "+srt);
                            devotionalList.put(srt,devotional);
                        }

                        populateData(jSon.getString("requestedDate"));
                    }
                }else{
                    Global.alertDialog(this,jSon.getString("title"),jSon.getString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                CodingMsg.l("event list"+e.getMessage());
            }
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.txtPrevDevotional){

            try {
                populateData(txtPrevDevotional.getText().toString());
                txtNextDevotional.setText(txtCurrentDevotional.getText().toString());
                txtCurrentDevotional.setText(txtPrevDevotional.getText().toString());

                c.setTime(devotionalDateFormat.parse(txtCurrentDevotional.getText().toString()));
                c.add(Calendar.DATE, -1);
                txtPrevDevotional.setText(devotionalDateFormat.format(c.getTimeInMillis()));
                CodingMsg.l("txtPrevDevotional: "+txtCurrentDevotional.getText().toString());
                CodingMsg.l("txtCurrentDevotional: "+txtCurrentDevotional.getText().toString());
                CodingMsg.l("txtNextDevotional: "+txtNextDevotional.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
         if(v.getId()==R.id.txtCurrentDevotional){

        }
         if(v.getId()==R.id.txtNextDevotional){
             try {
                 populateData(txtNextDevotional.getText().toString());
                 txtPrevDevotional.setText(txtCurrentDevotional.getText().toString());
                 txtCurrentDevotional.setText(txtNextDevotional.getText().toString());

                 c.setTime(devotionalDateFormat.parse(txtCurrentDevotional.getText().toString()));
                 c.add(Calendar.DATE, 1);
                 txtNextDevotional.setText(devotionalDateFormat.format(c.getTimeInMillis()));
                 CodingMsg.l("txtPrevDevotional: "+txtPrevDevotional.getText().toString());
                 CodingMsg.l("txtCurrentDevotional: "+txtCurrentDevotional.getText().toString());
                 CodingMsg.l("txtNextDevotional: "+txtNextDevotional.getText().toString());
             } catch (ParseException e) {
                 e.printStackTrace();
                 CodingMsg.l("prev error: "+e.getMessage());
             }
        }


    }
}

