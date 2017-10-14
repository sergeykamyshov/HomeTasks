package ru.sergeykamyshov.schedule.models;

import java.util.List;

/**
 * Класс описывает модель города со станциями
 */
public class City {

    private String mCountryTitle;
    private String mCityTitle;
    private List<Station> mStations;

    public City(String countryTitle, String cityTitle, List<Station> stations) {
        mCountryTitle = countryTitle;
        mCityTitle = cityTitle;
        mStations = stations;
    }

    public String getCountryTitle() {
        return mCountryTitle;
    }

    public void setCountryTitle(String countryTitle) {
        mCountryTitle = countryTitle;
    }

    public String getCityTitle() {
        return mCityTitle;
    }

    public void setCityTitle(String cityTitle) {
        mCityTitle = cityTitle;
    }

    public List<Station> getStations() {
        return mStations;
    }

    public void setStations(List<Station> stations) {
        mStations = stations;
    }
}
