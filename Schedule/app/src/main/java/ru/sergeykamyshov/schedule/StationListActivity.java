package ru.sergeykamyshov.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.sergeykamyshov.schedule.adapters.StationRecyclerAdapter;
import ru.sergeykamyshov.schedule.loaders.StationLoader;
import ru.sergeykamyshov.schedule.models.City;

import static ru.sergeykamyshov.schedule.loaders.StationLoader.STATION_LOADER_ID;

public class StationListActivity extends BaseActivity {

    private StationRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);

        // Подготавливаем Toolbar необоходим для размещения на нем поиска
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.action_bar_title_stations));

        // Подготавливаем RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.stations_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new StationRecyclerAdapter(this, new ArrayList<City>());
        recyclerView.setAdapter(mAdapter);

        // Инициируем работу Loader'a, который достанет нам данные из базы или json файла (если это первый запуск после обновления)
        getSupportLoaderManager().initLoader(STATION_LOADER_ID, null, mLoaderCallbacks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stations_list, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mAdapter != null) {
                    mAdapter.filterByQuery(newText);
                }
                return true;
            }
        });
        return true;
    }

    private LoaderManager.LoaderCallbacks<List<City>> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<City>>() {
        @Override
        public Loader<List<City>> onCreateLoader(int id, Bundle args) {
            return new StationLoader(getApplicationContext());
        }

        @Override
        public void onLoadFinished(Loader<List<City>> loader, List<City> data) {
            // Скрываем ProgressBar после выполнения возвращения результата от loader'а
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
    };
}
