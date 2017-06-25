package com.app.weather.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.weather.adapter.CityAdapter;
import com.app.weather.data.DatabaseHelper;
import com.app.weatherapp.R;

/**
 * Created by Sarah on 6/22/17.
 */
public class HomeFragment extends Fragment {

    private DatabaseHelper database;
    private CityAdapter mAdapter;
    private  RecyclerView mRecyclerView;
    private TextView emptyListViewMsg;
    private UpdateCitiesListBroadcastReceiver updateCitiesListBroadcastReceiver;
    public static final String UPDATE_CITIES_LIST_INTENT_ACTION = "update.cities.list.action";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container,
                false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        database = DatabaseHelper.getInstance(getContext());
        mAdapter = new CityAdapter(getContext(), database.getAllCities());
        mRecyclerView.setAdapter(mAdapter);
        emptyListViewMsg = (TextView) v.findViewById(R.id.add_new_city);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapFragment fragment = new MapFragment();
                getFragmentManager().beginTransaction()
                        .add(R.id.container, fragment)
                        .addToBackStack(fragment.getClass().getName())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });
        checkEmptyList();
        return v;
    }

    //show text hint if no cities in list
    private void checkEmptyList(){
        if(mAdapter.getItemCount() == 0){
            emptyListViewMsg.setVisibility(View.VISIBLE);
        }else{
            emptyListViewMsg.setVisibility(View.GONE);
        }
    }

    //update cities and check if we should display hint
    private void updateCities(){
        mAdapter = new CityAdapter(getContext(), database.getAllCities());
        mRecyclerView.setAdapter(mAdapter);
        checkEmptyList();
    }

    //use local broadcast receiver to update cities list
    @Override
    public void onResume() {
        super.onResume();
        updateCitiesListBroadcastReceiver = new UpdateCitiesListBroadcastReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateCitiesListBroadcastReceiver, new IntentFilter(HomeFragment.UPDATE_CITIES_LIST_INTENT_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateCitiesListBroadcastReceiver);
    }


    public class UpdateCitiesListBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateCities();
        }

    }
}
