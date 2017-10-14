package ru.sergeykamyshov.schedule.database;

import android.provider.BaseColumns;

/**
 * Класс описывающий структуру таблицы "station"
 */
public class StationSchema implements BaseColumns {

    public static final String TABLE_NAME = "station";

    public static final class Cols {
        public static final String COLUMN_COUNTRY_TITLE = "country_title";
        public static final String COLUMN_CITY_TITLE = "city_title";
        public static final String COLUMN_STATION_TITLE = "station_title";
    }
}
