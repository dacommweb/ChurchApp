package com.suntechnology.churchapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.suntechnology.churchapp.helper.Global;

import java.util.HashMap;

/**
 * Created by FRED on 7/12/2017.
 */
public class DefaultActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private NavigationView navigationView;
    private Menu menu;
    LinearLayout dailydevotionalayout,biblelayout,medialayout,eventslayout,locator,manualaout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_layout);
        init();
        initiviews();

    }

    private void initiviews() {
        locator = (LinearLayout) findViewById(R.id.locator);
        dailydevotionalayout = (LinearLayout) findViewById(R.id.dailydevotionalayout);
        biblelayout = (LinearLayout) findViewById(R.id.biblelayout);
        medialayout = (LinearLayout) findViewById(R.id.medialayout);
        eventslayout = (LinearLayout) findViewById(R.id.eventslayout);
        manualaout = (LinearLayout) findViewById(R.id.manualaout);
        viewsint();
    }

    public void viewsint(){
        dailydevotionalayout.setOnClickListener(this);
        biblelayout.setOnClickListener(this);
        medialayout.setOnClickListener(this);
        eventslayout.setOnClickListener(this);
        locator.setOnClickListener(this);
        manualaout.setOnClickListener(this);
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

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
        int id = item.getItemId();
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

        Global.MenusView(id,this,DefaultActivity.this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.biblelayout){
            Global.goToActivity(this,BibleActivity.class,new HashMap<String, String>());
        }else if(id == R.id.dailydevotionalayout){
            Global.goToActivity(this,DailyDevotional.class,new HashMap<String, String>());
        }else if(id == R.id.medialayout){
            Global.goToActivity(this,MediaActivity.class,new HashMap<String, String>());
        }else if(id == R.id.eventslayout){
            Global.goToActivity(this,EventsActivity.class,new HashMap<String, String>());
        }else if(id == R.id.locator){
            Global.goToActivity(this,LocatorActivatity.class,new HashMap<String, String>());
        }
//        else if(id == R.id.manualaout){
//            Global.goToClass(this,LocatorActivatity.class);
//        }
    }
}
