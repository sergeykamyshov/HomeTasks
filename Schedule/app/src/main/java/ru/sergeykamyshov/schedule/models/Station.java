package ru.sergeykamyshov.schedule.models;

/**
 * Класс описывает модель станции
 */
public class Station {

    private String mStationTitle;

    public Station(String stationTitle) {
        mStationTitle = stationTitle;
    }

    public String getStationTitle() {
        return mStationTitle;
    }

    public void setStationTitle(String stationTitle) {
        mStationTitle = stationTitle;
    }
}
