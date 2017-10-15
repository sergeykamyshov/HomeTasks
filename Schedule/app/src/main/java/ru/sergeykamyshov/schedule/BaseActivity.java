package ru.sergeykamyshov.schedule;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Общий класс для всех Activity приложения
 */
public class BaseActivity extends AppCompatActivity {

    public static final String PARAM_STATION_TITLE = "stationTitle";
    public static final String PARAM_CITY_TITLE = "stationCityTitle";
    public static final String PARAM_DISTRICT_TITLE = "stationDistrictTitle";
    public static final String PARAM_COUNTRY_TITLE = "stationCountryTitle";

    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }
}
