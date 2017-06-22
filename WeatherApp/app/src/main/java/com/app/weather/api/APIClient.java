package com.app.weather.api;

import android.content.Context;
import android.util.Log;

import com.app.weatherapp.R;

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

    public APIClient(Context context) {
        this.context = context;
    }

    public JSONObject getJSONObject(double lat, double lng) {
        try {
            URL url = new URL(APIUrl.CITY_WEATHER_URL + context.getString(R.string.open_weather_map_id) + "&lat=" + lat + "&lon=" + lng);
            Log.d("url is",".."+url.toString());
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
