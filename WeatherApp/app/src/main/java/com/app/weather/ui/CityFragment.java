package com.app.weather.ui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.weather.api.APIClient;
import com.app.weather.models.City;
import com.app.weather.models.Forecast;
import com.app.weatherapp.R;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Sarah on 6/22/17.
 */
public class CityFragment extends Fragment {


    private City city;
    private Forecast forecast;
    private APIClient apiClient;
    private TextView cityNameTextView,tempTextView,descriptionTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_city, container,
                false);
        Gson gson = new Gson();
        city = gson.fromJson(getArguments().getString("city"), City.class);
        apiClient = new APIClient(getContext());
        cityNameTextView = (TextView) v.findViewById(R.id.city_name);
        tempTextView = (TextView) v.findViewById(R.id.temp);
        descriptionTextView = (TextView) v.findViewById(R.id.description);

        loadWeatherData();
        return v;
    }

    private void setUIValues() {
        cityNameTextView.setText(city.getName());
        tempTextView.setText(forecast.getTemp()+"˚C");
        descriptionTextView.setText(forecast.getDescription());
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
                response = apiClient.getJSONObject(city.getLat(), city.getLng());
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
}
