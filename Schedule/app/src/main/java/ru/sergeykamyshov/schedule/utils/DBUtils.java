package ru.sergeykamyshov.schedule.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.sergeykamyshov.schedule.database.DBHelper;
import ru.sergeykamyshov.schedule.database.StationSchema;
import ru.sergeykamyshov.schedule.models.City;
import ru.sergeykamyshov.schedule.models.Station;

public class DBUtils {

    public static List<City> getStations(Context context) {
        List<City> stations;
        // TODO: добавить параметр куда-то
        // Если параметр включен, то обновляем данные из json файла
        if (false) {
            // Парсим данные из json файла
            stations = JSONUtils.fetchStationDataFromAssetsFile(context);
            // Сохраняем данные в базу
            writeStationsToDB(context, stations);
        } else {
            // Получаем данные из базы
            stations = readStationsFromDB(context);
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

    public static List<City> readStationsFromDB(Context context) {
        List<City> cities = new ArrayList<>();

        DBHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(StationSchema.TABLE_NAME, null, null, null, null, null, null);

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
