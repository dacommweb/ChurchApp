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
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntechnology.churchapp.classes.Devotional;
import com.suntechnology.churchapp.classes.MapWrapper;
import com.suntechnology.churchapp.fragment.DatePickerFragment;
import com.suntechnology.churchapp.helper.ApiRequest;
import com.suntechnology.churchapp.helper.CodingMsg;
import com.suntechnology.churchapp.helper.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by FRED on 7/14/2017.
 */

public class DailyDevotionalActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,ApiRequest.ApiRequestComm, View.OnClickListener,DatePickerFragment.DateCommunicator {

    private NavigationView navigationView;
    private Menu menu;
    private HashMap<String,Devotional> devotionalList=new HashMap<>();
    private Toolbar toolbar;
    private EditText etxtDate;
    private TextView txtNextDevotional,txtCurrentDevotional,txtPrevDevotional,
            txtTopic,txtVerse,txtContent,txtDeleteDev,txtAddDev,txtEditDev;
    private SimpleDateFormat devotionalDateFormat  = new SimpleDateFormat("MMMM,dd yyyy");
    private String currentDate="";
    private Devotional currentDevotional=new Devotional();
    Calendar c = Calendar.getInstance();
    private int devotionalSize=0;
    Gson gson = new Gson();
    private Dialog devotionalDialogue;
    private String devDate="";
    private String devTopic="";
    private String devVerse="";
    private String devContent="";
    private String currentId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailydevotional);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        initToolbar();
        CodingMsg.l("oncreate called");
//        HashMap<String, String> par=new HashMap<>();
//        par.put("action","get_devotional");
//        par.put("timestamp",TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "");
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
        //manage devotional
        txtDeleteDev = (TextView) findViewById(R.id.txtDeleteDev);
         txtEditDev = (TextView) findViewById(R.id.txtEditDev);
         txtAddDev = (TextView) findViewById(R.id.txtAddDev);


        txtDeleteDev.setOnClickListener(this);
        txtEditDev.setOnClickListener(this);
        txtAddDev.setOnClickListener(this);

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
           par.put("action","get_devotional_single");
           par.put("requestedDate",dateStr);

           new ApiRequest(this,par,true);
       }
    }
    private void showDevotionalDialogue() {
        devotionalDialogue = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        devotionalDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);
        devotionalDialogue.setContentView(R.layout.add_devotional_layout);
        devotionalDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        devotionalDialogue.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ImageView imgBack = (ImageView) devotionalDialogue.findViewById(R.id.imgBack);
         etxtDate = (EditText) devotionalDialogue.findViewById(R.id.etxtDate);
        final EditText etxtTopic = (EditText) devotionalDialogue.findViewById(R.id.etxtTopic);
        final EditText etxtVerse = (EditText) devotionalDialogue.findViewById(R.id.etxtVerse);
        final EditText etxtContent = (EditText) devotionalDialogue.findViewById(R.id.etxtContent);
        if(!currentId.contentEquals("")) {
            devDate=currentDevotional.getDate();
            etxtDate.setText(currentDevotional.getHdate());
            etxtTopic.setText(currentDevotional.getTitle());
            etxtVerse.setText(currentDevotional.getVerse());
            etxtContent.setText(currentDevotional.getContent());
        }
        Button btnSubmit = (Button) devotionalDialogue.findViewById(R.id.btnSubmit);
        etxtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerFragment().show(getSupportFragmentManager(), "datePicker");
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 devTopic=etxtTopic.getText().toString();
                 devVerse=etxtVerse.getText().toString();
                 devContent=etxtContent.getText().toString();
                if(devDate.length()<1){
                    etxtDate.setError("Choose date");
                    CodingMsg.t(DailyDevotionalActivity.this,"Choose Date");
                } else if(devTopic.length()<1){
                    etxtTopic.setError("Enter Topic");
                    CodingMsg.t(DailyDevotionalActivity.this,"Enter Topic");
                } else if(devVerse.length()<1){
                    CodingMsg.t(DailyDevotionalActivity.this,"Enter Verse");
                } else if(devContent.length()<1){
                    CodingMsg.t(DailyDevotionalActivity.this,"Enter ontent ");
                }else{
                    HashMap<String, String> par=new HashMap<>();
                    if(!currentId.contentEquals("")){
                        par.put("action","update_devotional");
                    }else{
                        par.put("action","add_devotional");
                    }

                    par.put("title",devTopic);
                    par.put("verse",devVerse);
                    par.put("content",devContent);
                    par.put("date",devDate);
                    new ApiRequest(DailyDevotionalActivity.this,par,true);
                }

            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devotionalDialogue.dismiss();
            }
        });
        devotionalDialogue.setCancelable(false);
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
    public Devotional parseDevotion(String str){
        Devotional devotional = new Devotional();
        try {
            JSONObject jSon1=new JSONObject(str);
            devotional.setId(jSon1.getString("id"));
            devotional.setTitle(jSon1.getString("devotional_title"));
            devotional.setVerse(jSon1.getString("devotional_verse"));
            devotional.setContent(jSon1.getString("devotional_content"));
            devotional.setDate(jSon1.getString("devotional_date"));
            devotional.setHdate(jSon1.getString("devotional_hdate"));
        } catch (JSONException e) {
            e.printStackTrace();
            CodingMsg.l("parseDevotion: "+e.getMessage());
        }

        return devotional;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Global.MenusView(id,this,DailyDevotionalActivity.this);
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
                    if (jSon.getString("action").equals("add_devotional")
                            || jSon.getString("action").equals("update_devotional")
                            ) {
                        Global.alertDialog(this,jSon.getString("title"),jSon.getString("message"));
                        devotionalDialogue.dismiss();
                        currentId="";
                    }
                    if (jSon.getString("action").equals("delete_devotional")) {
                        Global.alertDialog(this,jSon.getString("title"),jSon.getString("message"));
                    }
                    if (jSon.getString("action").equals("get_devotional_single")) {
                        if(Boolean.parseBoolean(jSon.getString("exist"))){
                            devotionalList.put(jSon.getString("requestedDate"),parseDevotion(jSon.getString("devotion")));
                            populateData(jSon.getString("requestedDate"));
                        }else{
                            Global.alertDialog(this,jSon.getString("title"),jSon.getString("message"));
                        }
                    }
                    if (jSon.getString("action").equals("get_devotional")) {
                        JSONArray jSone = new JSONArray(jSon.getString("devotionals"));
                        CodingMsg.l("JSONArray: "+jSone.length());
                        Devotional devotional;
                        JSONObject jSon1=new JSONObject();
                        for (int i = 0; i < jSone.length(); i++) {
                           // CodingMsg.l("JSONArray: "+i);
                            jSon1 = jSone.getJSONObject(i);
                            CodingMsg.l("json s"+jSone.toString());
                           // parseDevotion(jSon.getString("devotion"));
                            devotional = new Devotional();

                            //CodingMsg.l("devotional list"+jSon.toString());
                            devotional.setId(jSon1.getString("id"));
                            devotional.setTitle(jSon1.getString("devotional_title"));
                            devotional.setVerse(jSon1.getString("devotional_verse"));
                            devotional.setContent(jSon1.getString("devotional_content"));
                            devotional.setDate(jSon1.getString("devotional_date"));
                            devotional.setHdate(jSon1.getString("devotional_hdate"));
                            String srt= devotionalDateFormat.format(Double.parseDouble(jSon1.getString("devotional_date")+"000"));
                            CodingMsg.l("devotional_title: "+jSon1.getString("devotional_title")+"  "+srt);
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
        if(v.getId()==R.id.txtDeleteDev){
            currentDevotional=devotionalList.get(currentDate);
            HashMap<String, String> par=new HashMap<>();
            par.put("action","delete_devotional");
            par.put("id",currentDevotional.getId());
            new ApiRequest(this,par,true);
         }
         if(v.getId()==R.id.txtAddDev){
             showDevotionalDialogue();
         }
         if(v.getId()==R.id.txtEditDev){
             currentDevotional=devotionalList.get(currentDate);
             currentId=currentDevotional.getId();
             showDevotionalDialogue();
         }

         if(v.getId()==R.id.txtCurrentDevotional){

         }

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

    @Override
    public void updateDateTxt(Calendar cal) {
//        CodingMsg.l("Caal"+TimeUnit.MILLISECONDS.toSeconds(cal.getTimeInMillis()));
//        CodingMsg.l("Cal"+cal.getTimeInMillis());
//        CodingMsg.l("Cal"+devotionalDateFormat.format(new Date(cal.getTimeInMillis())));
        devDate= String.valueOf(TimeUnit.MILLISECONDS.toSeconds(cal.getTimeInMillis()));
        etxtDate.setText(devotionalDateFormat.format(new Date(cal.getTimeInMillis())));
    }
}

