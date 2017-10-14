package ru.sergeykamyshov.schedule.utils;

import android.app.Activity;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.sergeykamyshov.schedule.models.City;
import ru.sergeykamyshov.schedule.models.Station;

public class QueryUtils {

    public static final String STATIONS_JSON_FILE_LOCATION = "allStations.json";

    public static List<Station> fetchStationDataFromNetwork() {
        // Получаем данные по сети в формате json
        // ...
        // Если версия полученного json больше текущей, то обновляем базу данных (обновление ~ каждые 3 месяца)
        // ...
        return null;
    }

    /**
     * Достает данные из json файла через AssetManager и вызывает парсинг данных
     *
     * @param activity - activity списка, необходима для использования AssetManager
     * @return список городов со списком станций
     */
    public static List<City> fetchStationDataFromAssetsFile(Activity activity) {
        AssetManager assets = activity.getAssets();
        String jsonString;
        try {
            InputStream inputStream = assets.open(STATIONS_JSON_FILE_LOCATION);
            jsonString = getStringFromInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return extractStationsFromJSON(jsonString);
    }

    /**
     * Читает построчно json файл и возвращает полный результат в ввиде строки
     *
     * @param inputStream - json файл
     * @return строка содержащая значение json файла
     */
    private static String getStringFromInputStream(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * Парсит данные из json строки и возвращает список городов со станциями
     *
     * @param jsonString - строка в которой содержатся данные из json файла
     * @return список городов со списком станций
     */
    private static List<City> extractStationsFromJSON(String jsonString) {
        List<City> cities = new ArrayList<>();
        if (jsonString == null || jsonString.isEmpty()) {
            return cities;
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray citiesFromArray = jsonObject.getJSONArray("citiesFrom");
            for (int i = 0; i < citiesFromArray.length(); i++) {
                JSONObject city = (JSONObject) citiesFromArray.get(i);
                String countryTitle = city.getString("countryTitle");
                String cityTitle = city.getString("cityTitle");

                JSONArray stationsArray = city.getJSONArray("stations");
                List<Station> cityStations = new ArrayList<>();
                for (int j = 0; j < stationsArray.length(); j++) {
                    JSONObject cityStation = (JSONObject) stationsArray.get(j);
                    Station station = new Station(cityStation.getString("stationTitle"));
                    cityStations.add(station);
                }

                cities.add(new City(countryTitle, cityTitle, cityStations));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cities;
    }
}
