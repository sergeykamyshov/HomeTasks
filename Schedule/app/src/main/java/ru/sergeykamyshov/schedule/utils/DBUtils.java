package ru.sergeykamyshov.schedule.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.sergeykamyshov.schedule.database.DBHelper;
import ru.sergeykamyshov.schedule.database.StationSchema;
import ru.sergeykamyshov.schedule.models.City;
import ru.sergeykamyshov.schedule.models.Station;

import static ru.sergeykamyshov.schedule.database.StationSchema.Cols.COLUMN_DIRECTION_TYPE;

public class DBUtils {

    public static final String PREFERENCES_FILENAME = "db_preferences";
    public static final String PREFERENCES_IS_FIRST_LOAD = "isFirstLoad";

    public static final String LOG_TAG = DBUtils.class.getSimpleName();
    public static final String LOG_TAG_BACKGROUND_THREAD = DBUtils.class.getSimpleName() + " (Runnable)";

    public static List<City> getStations(final Context context, String directionType) {
        final List<City> stations;
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
        // При первом запуске необходимо обновить базу из json файла
        if (preferences.getBoolean(PREFERENCES_IS_FIRST_LOAD, true)) {
            Log.i(LOG_TAG, "SharedPreferences parameter \"isFirstLoad\"=true. Update database from JSON file");
            // TODO: при первом запуске в списке будут все станции. Необходимо отобрать только нужные
            Log.i(LOG_TAG, "Start to parse JSON file");
            // Парсим данные из json файла
            stations = JSONUtils.fetchStationDataFromAssetsFile(context);
            Log.i(LOG_TAG, "Run new background thread for database updade");
            // Сохраняем данные в базу
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.i(LOG_TAG_BACKGROUND_THREAD, "Start to update database");
                    writeStationsToDB(context, stations);
                    Log.i(LOG_TAG_BACKGROUND_THREAD, "Finish to update database");
                }
            }).start();
            Log.i(LOG_TAG, "Set SharedPreferences parameter \"isFirstLoad\"=false (don't need to update database)");
            // Устанавливаем флаг, что первый запуст успешно завершился
            preferences.edit().putBoolean(PREFERENCES_IS_FIRST_LOAD, false).apply();
        } else {
            Log.i(LOG_TAG, "Read station list from database with direction: " + directionType);
            // Получаем данные из базы
            stations = readStationsFromDB(context, directionType);
        }
        return stations;
    }

    public static void writeStationsToDB(Context context, List<City> cities) {
        DBHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        // Удаляем старые данные, чтобы полностью переписать новыми
        db.execSQL("DELETE FROM " + StationSchema.TABLE_NAME);

        for (City city : cities) {
            List<Station> stations = city.getStations();
            for (Station station : stations) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_DIRECTION_TYPE, city.getDirection());
                values.put(StationSchema.Cols.COLUMN_COUNTRY_TITLE, city.getCountryTitle());
                values.put(StationSchema.Cols.COLUMN_CITY_TITLE, city.getCityTitle());
                values.put(StationSchema.Cols.COLUMN_STATION_TITLE, station.getStationTitle());
                values.put(StationSchema.Cols.COLUMN_FULL_CITY_TITLE, station.getFullCityTitle());
                values.put(StationSchema.Cols.COLUMN_DISTRICT_TITLE, station.getDistrictTitle());
                // Каждую запись, каждого города, добавляем в БД
                db.insert(StationSchema.TABLE_NAME, null, values);
            }
        }
        db.close();
    }

    public static List<City> readStationsFromDB(Context context, String directionType) {
        List<City> cities = new ArrayList<>();

        DBHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        // Делаем выборку по типу направления. Тип передается параметром интента при переходе на StationListActivity
        Cursor cursor = db.query(StationSchema.TABLE_NAME, null,
                StationSchema.Cols.COLUMN_DIRECTION_TYPE + " = ?", new String[]{directionType}, null, null, null);

        if (cursor.moveToFirst()) {
            // Вычисляем индексы полей только один раз
            int countryColumnIndex = cursor.getColumnIndex(StationSchema.Cols.COLUMN_COUNTRY_TITLE);
            int cityColumnIndex = cursor.getColumnIndex(StationSchema.Cols.COLUMN_CITY_TITLE);
            int stationColumnIndex = cursor.getColumnIndex(StationSchema.Cols.COLUMN_STATION_TITLE);
            int fullCityColumnIndex = cursor.getColumnIndex(StationSchema.Cols.COLUMN_FULL_CITY_TITLE);
            int districtColumnIndex = cursor.getColumnIndex(StationSchema.Cols.COLUMN_DISTRICT_TITLE);

            do {
                String countryTitle = cursor.getString(countryColumnIndex);
                String cityTitle = cursor.getString(cityColumnIndex);
                // Если список пуст, то добавляем первый город с текущей станцией
                if (cities.isEmpty()) {
                    List<Station> cityStations = new ArrayList<>();
                    cityStations.add(new Station(cursor.getString(stationColumnIndex), cursor.getString(fullCityColumnIndex), cursor.getString(districtColumnIndex)));
                    cities.add(new City(countryTitle, cityTitle, cityStations));
                } else {
                    // Получаем последний добавленный город
                    City lastAddedCity = cities.get(cities.size() - 1);
                    // Если станция находится в последнем добавленом городе, то добавляем ее в тот же список
                    if (lastAddedCity.getCountryTitle().equals(countryTitle) && lastAddedCity.getCityTitle().equals(cityTitle)) {
                        lastAddedCity.getStations().add(new Station(cursor.getString(stationColumnIndex), cursor.getString(fullCityColumnIndex), cursor.getString(districtColumnIndex)));
                    } else {
                        // Создаем новый город с текущей станцией
                        List<Station> cityStations = new ArrayList<>();
                        cityStations.add(new Station(cursor.getString(stationColumnIndex), cursor.getString(fullCityColumnIndex), cursor.getString(districtColumnIndex)));
                        cities.add(new City(countryTitle, cityTitle, cityStations));
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return cities;
    }
}
