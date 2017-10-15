package ru.sergeykamyshov.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.sergeykamyshov.schedule.adapters.StationRecyclerAdapter;
import ru.sergeykamyshov.schedule.loaders.StationLoader;
import ru.sergeykamyshov.schedule.models.City;

import static ru.sergeykamyshov.schedule.loaders.StationLoader.STATION_LOADER_ID;

public class StationListActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<List<City>> {

    private StationRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);

        setActionBarTitle(getString(R.string.action_bar_title_stations));

        // Настраиваем RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.stations_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Создаем и устанавливаем адаптер для RecyclerView
        mAdapter = new StationRecyclerAdapter(this, new ArrayList<City>());
        recyclerView.setAdapter(mAdapter);

        // Загружаем данные из базы (или json файла если первый запуск после обновления)
        getSupportLoaderManager().initLoader(STATION_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<City>> onCreateLoader(int id, Bundle args) {
        return new StationLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<City>> loader, List<City> data) {
        // Скрываем ProgressBar после выполнения loader'а
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.bar_progress);
        progressBar.setVisibility(View.GONE);
        if (data != null && !data.isEmpty()) {
            mAdapter.setDataSet(data);
            mAdapter.notifyDataSetChanged();
        } else {
            // Если список пуст, то необходимо вывести информацию об этом
            TextView emptyList = (TextView) findViewById(R.id.text_empty_station_list);
            emptyList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<City>> loader) {
        mAdapter.setDataSet(new ArrayList<City>());
        mAdapter.notifyDataSetChanged();
    }
}
