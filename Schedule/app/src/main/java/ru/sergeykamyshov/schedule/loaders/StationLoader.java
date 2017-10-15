package ru.sergeykamyshov.schedule.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import ru.sergeykamyshov.schedule.models.City;
import ru.sergeykamyshov.schedule.utils.DBUtils;

/**
 * Loader управляющий загрузкой списка станций в фоновом потоке
 */
public class StationLoader extends AsyncTaskLoader<List<City>> {

    public static final int STATION_LOADER_ID = 1;

    public StationLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<City> loadInBackground() {
        return DBUtils.getStations(getContext());
    }
}
