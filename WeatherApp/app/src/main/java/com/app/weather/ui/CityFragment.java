package com.app.weather.ui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.util.ArrayList;
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
    private TextView cityNameTextView,countryTextView, tempTextView, descriptionTextView, humidityTextView, windTextView;

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
        forecastList = new ArrayList<>();
        loadWeatherData();
        loadNextDaysWeatherData();
        return v;
    }

    private void setUIValues() {
        cityNameTextView.setText(city.getName());
        countryTextView.setText(city.getCountry());
        tempTextView.setText(forecast.getTemp() + "˚C");
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
                    for(int i=0;i<response.length();i++){
                        Forecast forecast = new Forecast();
                        try {
                            forecast.parseJSONObject(response.getJSONObject(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    forecastList.add(forecast);
                } else {
                    Toast.makeText(getContext(), getString(R.string.problem_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }

        };
        task.execute((Void[]) null);
    }
}
