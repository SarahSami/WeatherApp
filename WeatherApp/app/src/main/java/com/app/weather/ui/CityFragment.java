package com.app.weather.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.weather.api.APIClient;
import com.app.weather.models.City;
import com.app.weather.models.Forecast;
import com.app.weather.util.ImageLoader;
import com.app.weatherapp.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sarah on 6/22/17.
 */
public class CityFragment extends Fragment {


    private City city;
    private Forecast forecast;
    private List<Forecast> forecastList;
    private APIClient apiClient;
    private ImageView iconView;
    private String unit;
    private LinearLayout forecastLayoutView;
    private TextView cityNameTextView, countryTextView, tempTextView, descriptionTextView, humidityTextView, windTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_city, container,
                false);
        Gson gson = new Gson();
        city = gson.fromJson(getArguments().getString("city"), City.class);
        apiClient = new APIClient(getContext());
        cityNameTextView = (TextView) v.findViewById(R.id.city_name);
        countryTextView = (TextView) v.findViewById(R.id.country_name);
        tempTextView = (TextView) v.findViewById(R.id.temp);
        descriptionTextView = (TextView) v.findViewById(R.id.description);
        humidityTextView = (TextView) v.findViewById(R.id.humidity);
        windTextView = (TextView) v.findViewById(R.id.wind);
        iconView = (ImageView) v.findViewById(R.id.icon);
        forecastLayoutView = (LinearLayout) v.findViewById(R.id.forecast_list);
        forecastList = new ArrayList<>();
        loadWeatherData();
        loadNextDaysWeatherData();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String unitKey = sharedPreferences.getString(SettingsFragment.UNIT_KEY, "");
        if (unitKey.equals(SettingsFragment.UNIT_METRIC_KEY))
            unit = "˚C";
        else
            unit = "˚F";
        return v;
    }

    private void setUIValues() {
        cityNameTextView.setText(city.getName());
        countryTextView.setText(city.getCountry());
        tempTextView.setText(forecast.getTemp() + unit);
        descriptionTextView.setText(forecast.getDescription());
        humidityTextView.setText(forecast.getHumidity() + "%");
        windTextView.setText(forecast.getWindSpeed() + "m/s");

        ImageLoader imageLoader = new ImageLoader();
        imageLoader.downloadImage(iconView, forecast.getIcon());
    }

    private void loadWeatherData() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            ProgressDialog pd;
            JSONObject response;

            @Override
            protected void onPreExecute() {
                pd = new ProgressDialog(getContext());
                pd.setMessage(getString(R.string.please_wait));
                pd.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                response = apiClient.getWeatherData(city.getLat(), city.getLng());
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response != null) {
                    forecast = new Forecast();
                    forecast.parseJSONObject(response);
                    setUIValues();
                } else {
                    Toast.makeText(getContext(), getString(R.string.problem_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }

        };
        task.execute((Void[]) null);
    }


    private void loadNextDaysWeatherData() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            JSONArray response;

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected Void doInBackground(Void... params) {
                response = apiClient.getNextDaysWeatherData(city.getLat(), city.getLng());
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (response != null) {
                    //api return 8 weather data for each data so we get one only
                    //skip the first day weather data as we have it already updated in realtime
                    for (int i = 11; i < response.length(); i = i + 8) {
                        Forecast forecast = new Forecast();
                        try {
                            forecast.parseJSONObject(response.getJSONObject(i));
                            forecastList.add(forecast);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    loadForecastData();
                } else {
                    Toast.makeText(getContext(), getString(R.string.problem_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }

        };
        task.execute((Void[]) null);
    }

    /*
    * load 5-days forecast data and add each view to linear layout so that we can handle scroll
    * */
    private void loadForecastData() {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < forecastList.size(); i++) {
            Forecast forecast = forecastList.get(i);
            View itemView = inflater.inflate(R.layout.list_item_forecast, forecastLayoutView, false);

            TextView dayView = (TextView) itemView.findViewById(R.id.day);
            TextView dateView = (TextView) itemView.findViewById(R.id.date);
            TextView tempView = (TextView) itemView.findViewById(R.id.temp);
            TextView descriptionView = (TextView) itemView.findViewById(R.id.description);
            TextView humidityView = (TextView) itemView.findViewById(R.id.humidity);
            TextView windView = (TextView) itemView.findViewById(R.id.wind);
            ImageView iconView = (ImageView) itemView.findViewById(R.id.icon);

            dayView.setText(getDayName(forecast.getDate()));
            dateView.setText(formatDate(forecast.getDate()));
            tempView.setText(forecast.getTemp() + unit);
            descriptionView.setText(forecast.getDescription());
            humidityView.setText(forecast.getHumidity() + "%");
            windView.setText(forecast.getWindSpeed() + "m/s");
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.downloadImage(iconView, forecast.getIcon());
            forecastLayoutView.addView(itemView);

        }


    }

    private String getDayName(String day) {
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = inFormat.parse(day);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            return outFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String formatDate(String day) {
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = inFormat.parse(day);
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");

            return dayFormat.format(date) + "/" + monthFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
