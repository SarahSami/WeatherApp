package com.app.weather.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.weather.data.DatabaseHelper;
import com.app.weather.models.City;
import com.app.weather.ui.HomeFragment;
import com.app.weatherapp.R;
import com.google.gson.Gson;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    public static final String START_FRAGMENT_CITY_INTENT_ACTION = "start.fragment.city.action";
    private List<City> cities;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cityNameView, countryNameView, removeCityView;
        public CardView cardView;

        public ViewHolder(View v) {
            super(v);
            cityNameView = (TextView) itemView.findViewById(R.id.city_name);
            countryNameView = (TextView) itemView.findViewById(R.id.country_name);
            removeCityView = (TextView) itemView.findViewById(R.id.remove_city);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

        }
    }

    public CityAdapter(Context context, List<City> data) {
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
        holder.countryNameView.setText(cities.get(position).getCountry());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(START_FRAGMENT_CITY_INTENT_ACTION);
                Gson gson = new Gson();
                intent.putExtra("city", gson.toJson(cities.get(position)));
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            }
        });

        holder.removeCityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCityPermission(cities.get(position));
            }
        });

    }

    private void removeCityPermission(final City selectedCity) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                mContext);
        myAlertDialog.setMessage(mContext.getString(R.string.delete_city));
        myAlertDialog.setPositiveButton(mContext.getString(R.string.yes),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        removeCity(selectedCity);
                        return;
                    }
                });
        myAlertDialog.setNegativeButton(mContext.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        return;
                    }
                });
        myAlertDialog.show();
    }

    private void removeCity(City city) {
        DatabaseHelper db = DatabaseHelper.getInstance(mContext);
        db.removeCity(city.getName());
        Intent intent = new Intent(HomeFragment.UPDATE_CITIES_LIST_INTENT_ACTION);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

}