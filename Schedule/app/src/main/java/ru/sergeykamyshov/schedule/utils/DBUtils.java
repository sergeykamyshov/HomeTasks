package ru.sergeykamyshov.schedule.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.sergeykamyshov.schedule.database.DBHelper;
import ru.sergeykamyshov.schedule.database.StationSchema;
import ru.sergeykamyshov.schedule.models.City;
import ru.sergeykamyshov.schedule.models.Station;

import static ru.sergeykamyshov.schedule.database.StationSchema.Cols.COLUMN_DIRECTION_TYPE;

/**
 * Класс утилита для работы с базой данных
 */
public class DBUtils {

    /**
     * Наименование файла SharedPreferences
     */
    public static final String PREFERENCES_FILENAME = "db_preferences";
    /**
     * Флаг необходимости обновления. Если true, то данные будут парситься из json файла и сохраняться в БД,
     * если false, то данные будут только читаться из БД
     */
    public static final String PREFERENCES_NEED_UPDATE = "need_update";
    /**
     * Флаг завершения обновления БД. Если true, то можно запускать еще одно обновление БД,
     * если false, то запрос на обновление будет проигнорирован
     */
    public static final String PREFERENCES_UPDATE_COMPLETED = "update_completed";

    /**
     * Возвращает список городов со станциями. При первом запуске приложения данные будут парситься
     * из json файла и сохраняться в базу. Последующие запуски будут читать данные прямо из базы.
     *
     * @param context       - контекст для доступа к ресурсам
     * @param directionType - тип направления станций
     * @return список городов со станциями запрашиваемого направления
     */
    public static List<City> getStations(final Context context, String directionType) {
        List<City> stations;
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
        // При первом запуске необходимо обновить базу из json файла
        if (preferences.getBoolean(PREFERENCES_NEED_UPDATE, true)) {
            // Парсим данные из json файла
            final List<City> allStations = JSONUtils.fetchStationDataFromAssetsFile(context);
            // Отбирает только данные с направлением, которые мы запрашивали
            stations = getStationsByDirection(allStations, directionType);
            // Сохраняем полный список данных в базу в отдельном потоке, чтобы не мешать пользователю
            new Thread(new Runnable() {
                @Override
                public void run() {
                    writeStationsToDB(context, allStations);
                }
            }).start();
            // Устанавливаем флаг, что данные получены и обновление пока не требуется
            preferences.edit().putBoolean(PREFERENCES_NEED_UPDATE, false).apply();
        } else {
            // Получаем данные из базы
            stations = readStationsFromDB(context, directionType);
        }
        return stations;
    }

    /**
     * Возвращает список городов со станциями указанного направления
     *
     * @param allStations   - полный список городов со станциями
     * @param directionType - тип направления станций
     * @return список городов со станциями запрашиваемого направления
     */
    private static List<City> getStationsByDirection(List<City> allStations, String directionType) {
        List<City> stations = new ArrayList<>();
        for (City city : allStations) {
            if (directionType.equals(city.getDirection())) {
                stations.add(city);
            }
        }
        return stations;
    }

    /**
     * Записывает полученные станции городов в таблицу. Очищает предыдущие данные в таблице
     *
     * @param context - контекст для подключения к базе данных
     * @param cities  - список городов со станциями
     */
    public static void writeStationsToDB(Context context, List<City> cities) {
        // Устанавливаем флаг, что обновление базы данных не закончилось, т.к. оно только началось
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCES_UPDATE_COMPLETED, false).apply();

        DBHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        // Удаляем старые данные, чтобы полностью переписать новыми
        db.execSQL("DELETE FROM " + StationSchema.TABLE_NAME);
        for (City city : cities) {
            for (Station station : city.getStations()) {
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
        // Устанавливаем флаг, что обновление базы данных завершенно
        preferences.edit().putBoolean(PREFERENCES_UPDATE_COMPLETED, true).apply();
    }

    /**
     * Возврщает список городов со станциями из базы данных по переданному типу направления
     *
     * @param context       - контекст для подключения к базе данных
     * @param directionType - тип направления станций
     * @return список городов со станциями запрашиваемого направления
     */
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
