package com.app.weather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import com.app.weather.ui.SettingsFragment;

import java.util.Locale;

/**
 * Created by Sarah on 6/25/17.
 */
public class Util {

    public static void loadLanguage(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String lang = preferences.getString(SettingsFragment.LANGUAGE_KEY, SettingsFragment.LANGUAGE_ENGLISH_KEY);
        Locale locale;
        if(lang.equals(SettingsFragment.LANGUAGE_ENGLISH_KEY))
            locale = new Locale("en_US");
        else{
            locale = new Locale("ar");

        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);

    }
}
