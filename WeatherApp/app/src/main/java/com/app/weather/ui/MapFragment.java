package com.app.weather.ui;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.weather.data.DatabaseHelper;
import com.app.weather.models.City;
import com.app.weatherapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sarah on 6/22/17.
 */
public class MapFragment extends Fragment {


    private GoogleMap googleMap;
    private MarkerOptions marker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container,
                false);


        v.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCity();
            }
        });

        v.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishFragment();
            }
        });
        MapView mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomGesturesEnabled(true);

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        marker = new MarkerOptions().position(
                                latLng);

                        googleMap.addMarker(marker);
                    }
                });


            }
        });

        return v;
    }

    private void addCity() {
        if (marker != null) {
            LatLng location = marker.getPosition();
            City city = new City();
            city.setLat(location.latitude);
            city.setLng(location.longitude);

            Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(location.latitude, location.longitude, 1);
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    city.setCountry(address.getCountryName());
                    String cityName = address.getLocality();
                    if (cityName == null)
                        cityName = address.getCountryName();
                    city.setName(cityName);
                    DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
                    db.addCity(city);
                    Toast.makeText(getActivity(), city.getName() + "," + city.getCountry() + " added", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeFragment.UPDATE_CITIES_LIST_INTENT_ACTION);
                    getContext().sendBroadcast(intent);

                    finishFragment();
                }
            } catch (IOException e) {
                Toast.makeText(getActivity(), getString(R.string.no_location_found), Toast.LENGTH_SHORT).show();

            }


        } else {
            Toast.makeText(getActivity(), getString(R.string.no_location_found), Toast.LENGTH_SHORT).show();
        }

    }

    private void finishFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(this);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }
}
