package com.app.weather.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.app.weather.data.DatabaseHelper;
import com.app.weatherapp.R;

/**
 * Created by Sarah on 6/24/17.
 */
public class SettingsFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    public static final String LANGUAGE_ENGLISH_KEY = "english";
    public static final String LANGUAGE_ARABIC_KEY = "arabic";
    public static final String UNIT_METRIC_KEY = "metric";
    public static final String UNIT_IMPERIAL_KEY = "imperial";
    public static final String UNIT_KEY = "unit";
    public static final String LANGUAGE_KEY = "language";
    private static final String EMAIL_DEVELOPER = "sarahsami198@gmail.com";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_settings, container,
                false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        v.findViewById(R.id.delete_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAllCitiesPermission();
            }
        });

        RadioButton metricButton = (RadioButton) v.findViewById(R.id.metric);
        RadioButton imperialButton = (RadioButton) v.findViewById(R.id.imperial);
        RadioButton arabicButton = (RadioButton) v.findViewById(R.id.arabic);
        RadioButton englishButton = (RadioButton) v.findViewById(R.id.english);

        String unitKey = sharedPreferences.getString(UNIT_KEY, "");
        if (unitKey.equals(UNIT_METRIC_KEY))
            metricButton.setChecked(true);
        else
            imperialButton.setChecked(true);

        String languageKey = sharedPreferences.getString(LANGUAGE_KEY, "");
        if (languageKey.equals(LANGUAGE_ARABIC_KEY))
            arabicButton.setChecked(true);
        else
            englishButton.setChecked(true);

        metricButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    changeUnitSystem(UNIT_METRIC_KEY);
                }
            }
        });

        imperialButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    changeUnitSystem(UNIT_IMPERIAL_KEY);
                }
            }
        });

        arabicButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    changeLanguage(LANGUAGE_ARABIC_KEY);
                }
            }
        });

        englishButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    changeLanguage(LANGUAGE_ENGLISH_KEY);
                }
            }
        });

        v.findViewById(R.id.contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });



        return v;
    }

    private void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL_DEVELOPER});
        try {
            startActivity(i);
        } catch (android.content.ActivityNotFoundException ex) {
        }
    }

    private void changeLanguage(String language) {
        sharedPreferences.edit().putString(LANGUAGE_KEY, language).commit();
        resetApp();
    }

    private void changeUnitSystem(String unit) {
        sharedPreferences.edit().putString(UNIT_KEY, unit).commit();

    }

    private void resetApp() {
        getActivity().finish();
        getActivity().startActivity(new Intent(getContext(), MainActivity.class));
    }

    private void removeAllCitiesPermission() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                getContext());
        myAlertDialog.setMessage(getString(R.string.delete_all_cities));
        myAlertDialog.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        removeAllCities();
                        return;
                    }
                });
        myAlertDialog.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        return;
                    }
                });
        myAlertDialog.show();
    }

    private void removeAllCities() {
        DatabaseHelper db = DatabaseHelper.getInstance(getContext());
        db.removeAllCities();
        Intent intent = new Intent(HomeFragment.UPDATE_CITIES_LIST_INTENT_ACTION);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }
}
