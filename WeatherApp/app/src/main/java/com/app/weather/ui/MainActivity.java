package com.app.weather.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.app.weather.adapter.CityAdapter;
import com.app.weather.data.DatabaseHelper;
import com.app.weather.util.Util;
import com.app.weatherapp.R;

import java.io.IOException;

/**
 * Created by Sarah on 6/22/17.
 */
public class MainActivity extends AppCompatActivity {

    private SelectCityBroadcastReceiver selectCityBroadcastReceiver;

    /*
    * load app language and create database if not exists
    * show home fragment
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Util.loadLanguage(this);
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        try {
            db.createDataBase();
        } catch (IOException ioe) {

            ioe.printStackTrace();
        }
        try {
            db.openDataBase();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }


        if (savedInstanceState == null) {
            HomeFragment fragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(fragment.getClass().getName())
                    .add(R.id.container, fragment)
                    .commit();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.help:
                help();
                break;
            case R.id.settings:
                settings();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void help() {
        HelpFragment fragment = new HelpFragment();
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(fragment.getClass().getName())
                .add(R.id.container, fragment)
                .commit();

    }

    private void settings() {
        SettingsFragment fragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(fragment.getClass().getName())
                .add(R.id.container, fragment)
                .commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        selectCityBroadcastReceiver = new SelectCityBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(selectCityBroadcastReceiver, new IntentFilter(CityAdapter.START_FRAGMENT_CITY_INTENT_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(selectCityBroadcastReceiver);
    }


    /*
    * onback press close current fragment if open or close app
    * */
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count <= 1) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    public class SelectCityBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            CityFragment fragment = new CityFragment();
            fragment.setArguments(intent.getExtras());
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(fragment.getClass().getName())
                    .add(R.id.container, fragment)
                    .commit();
        }

    }

}
