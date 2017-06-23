package com.app.weather.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sarah on 6/23/17.
 */
public class Forecast {

    private String main, description, icon, date;
    private double temp, windSpeed, windDegree, rainVolume;
    private long sunrise, sunset;
    private int humidity;

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(double windDegree) {
        this.windDegree = windDegree;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public double getRainVolume() {
        return rainVolume;
    }

    public void setRainVolume(double rainVolume) {
        this.rainVolume = rainVolume;
    }

    public void parseJSONObject(JSONObject object) {
        try {
            JSONObject weather = object.getJSONArray("weather").getJSONObject(0);
            JSONObject main = object.getJSONObject("main");

            this.main = weather.getString("main");
            this.description = weather.getString("description");
            this.icon = weather.getString("icon");

            this.temp = main.getDouble("temp");
            this.humidity = main.getInt("humidity");

            this.windSpeed = object.getJSONObject("wind").getDouble("speed");
            this.windDegree = object.getJSONObject("wind").getDouble("deg");

            this.sunrise = object.getJSONObject("sys").getLong("sunrise");
            this.sunset = object.getJSONObject("sys").getLong("sunset");


        } catch (JSONException e) {
        }

        this.date = object.optString("dt_txt");
        try {
            this.rainVolume = object.getJSONObject("rain").getDouble("3h");
        } catch (Exception e) {
        }

    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
