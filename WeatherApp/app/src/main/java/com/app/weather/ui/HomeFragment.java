package com.app.weather.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.weather.adapter.CityAdapter;
import com.app.weather.data.DatabaseHelper;
import com.app.weatherapp.R;

/**
 * Created by Sarah on 6/22/17.
 */
public class HomeFragment extends Fragment {

    private DatabaseHelper database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container,
                false);

        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        database = DatabaseHelper.getInstance(getContext());
        CityAdapter mAdapter = new CityAdapter(getContext(),database.getAllCities());
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .add(R.id.container, new MapFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        return v;
    }
}
