package com.app.weather.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.app.weather.adapter.CityAdapter;
import com.app.weather.data.DatabaseHelper;
import com.app.weatherapp.R;

import java.io.IOException;

/**
 * Created by Sarah on 6/22/17.
 */
public class MainActivity extends FragmentActivity {

    private SelectCityBroadcastReceiver selectCityBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new HomeFragment())
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
        if (item.getItemId() == R.id.add_city) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MapFragment())
                    .commit();
        }
        return false;
    }

    public void addCity(View v) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new MapFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        selectCityBroadcastReceiver = new SelectCityBroadcastReceiver();
        registerReceiver(selectCityBroadcastReceiver, new IntentFilter(CityAdapter.START_FRAGMENT_CITY_INTENT_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(selectCityBroadcastReceiver);
    }


    public class SelectCityBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            CityFragment fragment = new CityFragment();
            fragment.setArguments(intent.getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

    }

}
