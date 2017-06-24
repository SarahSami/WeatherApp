package com.app.weather.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.app.weather.ui.SettingsFragment;
import com.app.weatherapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sarah on 6/22/17.
 */
public class APIClient {


    private Context context;
    private String unit;

    public APIClient(Context context) {
        this.context = context;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        unit = sharedPreferences.getString(SettingsFragment.UNIT_KEY, "");
    }

    public JSONObject getWeatherData(double lat, double lng) {
        String url = APIUrl.CITY_WEATHER_URL + context.getString(R.string.open_weather_map_id) + "&lat=" + lat + "&lon=" + lng
                + "&units=" + unit;

        return getJSONObject(url);
    }

    public JSONArray getNextDaysWeatherData(double lat, double lng) {
        String url = APIUrl.CITY_FORECAST_URL + context.getString(R.string.open_weather_map_id) + "&lat=" + lat + "&lon=" + lng
                + "&units=" + unit;
        JSONObject jsonObject = getJSONObject(url);
        if (jsonObject != null) {
            try {
                return jsonObject.getJSONArray("list");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private JSONObject getJSONObject(String baseUrl) {
        try {
            URL url = new URL(baseUrl);
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            return data;
        } catch (Exception e) {
            return null;
        }
    }
}
