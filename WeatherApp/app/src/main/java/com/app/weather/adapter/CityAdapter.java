package com.app.weather.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.weather.models.City;
import com.app.weatherapp.R;
import com.google.gson.Gson;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    public static final String START_FRAGMENT_CITY_INTENT_ACTION = "start.fragment.city.action";
    private List<City> cities;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cityNameView;

        public ViewHolder(View v) {
            super(v);
            cityNameView = (TextView) itemView.findViewById(R.id.city_name);
        }
    }

    public CityAdapter(Context context,List<City> data) {
        cities = data;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_city, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.cityNameView.setText(cities.get(position).getName());
        holder.cityNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(START_FRAGMENT_CITY_INTENT_ACTION);
                Gson gson = new Gson();
                intent.putExtra("city", gson.toJson(cities.get(position)));
                mContext.sendBroadcast(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

}