package ru.sergeykamyshov.schedule.models;

/**
 * Класс описывает модель станции
 */
public class Station {

    private String mStationTitle;
    private String mFullCityTitle;
    private String mDistrictTitle;

    public Station(String stationTitle, String fullCityTitle, String districtTitle) {
        mStationTitle = stationTitle;
        mFullCityTitle = fullCityTitle;
        mDistrictTitle = districtTitle;
    }

    public String getStationTitle() {
        return mStationTitle;
    }

    public void setStationTitle(String stationTitle) {
        mStationTitle = stationTitle;
    }

    public String getFullCityTitle() {
        return mFullCityTitle;
    }

    public void setFullCityTitle(String fullCityTitle) {
        mFullCityTitle = fullCityTitle;
    }

    public String getDistrictTitle() {
        return mDistrictTitle;
    }

    public void setDistrictTitle(String districtTitle) {
        mDistrictTitle = districtTitle;
    }
}
