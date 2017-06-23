package com.app.weather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.weather.models.Forecast;
import com.app.weather.util.ImageLoader;
import com.app.weatherapp.R;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {
    private List<Forecast> forecastList;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dayView, dateView, tempView, descriptionView, humidityView, windView;
        public ImageView iconView;

        public ViewHolder(View v) {
            super(v);
            dayView = (TextView) itemView.findViewById(R.id.day);
            dateView = (TextView) itemView.findViewById(R.id.date);
            tempView = (TextView) itemView.findViewById(R.id.temp);
            descriptionView = (TextView) itemView.findViewById(R.id.description);
            humidityView = (TextView) itemView.findViewById(R.id.humidity);
            windView = (TextView) itemView.findViewById(R.id.wind);
            iconView = (ImageView) itemView.findViewById(R.id.icon);

        }
    }

    public ForecastAdapter(Context context, List<Forecast> data) {
        forecastList = data;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_forecast, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Forecast forecast = forecastList.get(position);
        holder.tempView.setText(forecast.getTemp() + "˚C");
        holder.descriptionView.setText(forecast.getDescription());
        holder.humidityView.setText(forecast.getHumidity() + "%");
        holder.windView.setText(forecast.getWindSpeed() + "m/s");
        ImageLoader imageLoader = new ImageLoader();
        imageLoader.downloadImage(holder.iconView, forecast.getIcon());
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

}