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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntechnology.churchapp.adapters.EventRVAdapter;
import com.suntechnology.churchapp.classes.Events;
import com.suntechnology.churchapp.fragment.DatePickerFragment;
import com.suntechnology.churchapp.helper.ApiRequest;
import com.suntechnology.churchapp.helper.CodingMsg;
import com.suntechnology.churchapp.helper.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by FRED on 7/14/2017.
 */

public class EventsActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,ApiRequest.ApiRequestComm, View.OnClickListener,EventRVAdapter.EventComm,DatePickerFragment.DateCommunicator {

    private NavigationView navigationView;
    private Menu menu;
    private ArrayList<Events> eventList=new ArrayList<>() ;
    private EventRVAdapter adapterTRV;
    private RecyclerView eventRV;
    private Toolbar toolbar;
    private TextView txtAddEvent;
    private EditText etxtDate;
    private Dialog eventDialogue;
    private String currentId="";
    private Events currentEvent;
    private String eventTitle;
    private String eventDate;
    private String eventDesc;
    private SimpleDateFormat eventDateFormat  = new SimpleDateFormat("MMMM,dd yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        initToolbar();
        HashMap<String, String> par=new HashMap<>();
        par.put("action","get_events");
        new ApiRequest(this,par,true);
        //initRV();
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

    private void initRV(){
        adapterTRV = new EventRVAdapter(this, eventList);
        eventRV.setLayoutManager(new LinearLayoutManager(this));
        eventRV.setAdapter(adapterTRV);
    }
    private void init() {
         toolbar = (Toolbar) findViewById(R.id.toolbar);
         eventRV = (RecyclerView) findViewById(R.id.eventRV);

        txtAddEvent = (TextView) findViewById(R.id.txtAddEvent);
        txtAddEvent.setOnClickListener(this);
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
        Global.MenusView(id,this,EventsActivity.this);
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
                    if (jSon.getString("action").equals("get_events")) {
                        CodingMsg.l("get_events");
                        JSONArray jSone = new JSONArray(jSon.getString("events"));
                        for (int i = 0; i < jSone.length(); i++) {

                            Events  event = new Events();
                            jSon = jSone.getJSONObject(i);
                            CodingMsg.l("event list"+jSon.toString());
                            event.setEventId(jSon.getString("id"));
                            event.setEventTitle(jSon.getString("event_title"));
                            event.setEventDesc(jSon.getString("event_desc"));
                            event.setEventDate(jSon.getString("event_date"));

                            eventList.add(event);
                        }

                       initRV();
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

        if(v.getId()==R.id.txtAddEvent){
            showEventDialogue();
        }
        
    }

    @Override
    public void eventMessage(String action, Events event) {
        if(action.contentEquals("edit")){
            currentId=event.getEventId();
            currentEvent=event;
            showEventDialogue();
        }
        if(action.contentEquals("delete")){
            HashMap<String, String> par=new HashMap<>();
            par.put("action","delete_events");
            par.put("id",event.getEventId());
            new ApiRequest(this,par,true);
        }

    }
    private void showEventDialogue() {
        eventDialogue = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        eventDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);
        eventDialogue.setContentView(R.layout.add_event);
        eventDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        eventDialogue.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ImageView imgBack = (ImageView) eventDialogue.findViewById(R.id.imgBack);
        etxtDate = (EditText) eventDialogue.findViewById(R.id.etxtDate);
         TextView dialogueTitle = (TextView) eventDialogue.findViewById(R.id.dialogueTitle);
        final EditText etxtTitle = (EditText) eventDialogue.findViewById(R.id.etxtTitle);
        final EditText etxtDesc = (EditText) eventDialogue.findViewById(R.id.etxtDesc);
        dialogueTitle.setText("Add Event");
        if(!currentId.contentEquals("")) {
            dialogueTitle.setText("Update Event");
            eventDate=currentEvent.getEventDate();
            etxtDate.setText(eventDateFormat.format(Double.parseDouble(eventDate+"000")));
            etxtTitle.setText(currentEvent.getEventTitle());
            etxtDesc.setText(currentEvent.getEventDesc());
        }

        Button btnSubmit = (Button) eventDialogue.findViewById(R.id.btnSubmit);
        etxtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerFragment().show(getSupportFragmentManager(), "datePicker");
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventTitle=etxtTitle.getText().toString();
                eventDesc=etxtDesc.getText().toString();
                if(eventDate.length()<1){
                    etxtDate.setError("Choose date");
                    CodingMsg.t(EventsActivity.this,"Choose Date");
                } else if(eventTitle.length()<1){
                    etxtTitle.setError("Enter Title");
                    CodingMsg.t(EventsActivity.this,"Enter Topic");
                } else if(eventDesc.length()<1){
                    CodingMsg.t(EventsActivity.this,"Enter Content ");
                }else{
                    HashMap<String, String> par=new HashMap<>();
                    if(!currentId.contentEquals("")){
                        par.put("action","update_event");
                        par.put("id",currentId);
                    }else{
                        par.put("action","add_event");
                    }

                    par.put("title",eventTitle);
                    par.put("content",eventDesc);
                    par.put("date",eventDate);
                    new ApiRequest(EventsActivity.this,par,true);
                }

            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDialogue.dismiss();
            }
        });
        eventDialogue.setCancelable(false);
        eventDialogue.show();
    }

    @Override
    public void updateDateTxt(Calendar cal) {
        eventDate= String.valueOf(TimeUnit.MILLISECONDS.toSeconds(cal.getTimeInMillis()));
        etxtDate.setText(eventDateFormat.format(new Date(cal.getTimeInMillis())));
    }
}

