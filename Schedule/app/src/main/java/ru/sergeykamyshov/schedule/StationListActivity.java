package ru.sergeykamyshov.schedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import ru.sergeykamyshov.schedule.models.City;
import ru.sergeykamyshov.schedule.utils.DBUtils;

import static ru.sergeykamyshov.schedule.ScheduleActivity.EXTRA_PARAM_DIRECTION_TYPE;
import static ru.sergeykamyshov.schedule.utils.DBUtils.PREFERENCES_FILENAME;
import static ru.sergeykamyshov.schedule.utils.DBUtils.PREFERENCES_NEED_UPDATE;
import static ru.sergeykamyshov.schedule.utils.DBUtils.PREFERENCES_UPDATE_COMPLETED;

/**
 * Класс отвечает за обработку на экране "Станции"
 */
public class StationListActivity extends BaseActivity {

    private StationRecyclerAdapter mAdapter;
    private String directionTypeParam;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.action_bar_title_stations));
        enableToolbarBackButton();

        // Подготавливаем RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.stations_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new StationRecyclerAdapter(this, new ArrayList<City>());
        recyclerView.setAdapter(mAdapter);

        // Будем загружать только тот список, который запрашивают ("from" или "to")
        directionTypeParam = getIntent().getStringExtra(EXTRA_PARAM_DIRECTION_TYPE);
        new StationAsyncTask().execute(directionTypeParam);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_update_db) {
            SharedPreferences preferences = getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
            // Проверяем что прошлое обновление базы завершилось. Если нет, то игнорируем
            if (preferences.getBoolean(PREFERENCES_UPDATE_COMPLETED, false)) {
                // Меняем флаг "need_update", чтобы обновить данные из json файла
                preferences.edit().putBoolean(PREFERENCES_NEED_UPDATE, true).apply();
                // Запускаем обновление данных в адаптере и в базе
                new StationAsyncTask().execute(directionTypeParam);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Класс запускает загрузку данных в фоновом потоке. После выполнения обновляет данные адаптера
     */
    private class StationAsyncTask extends AsyncTask<String, Void, List<City>> {

        @Override
        protected List<City> doInBackground(String... params) {
            // В params[0] передается тип направления, по которому будет сделана выборка в базе
            return DBUtils.getStations(getApplicationContext(), params[0]);
        }

        @Override
        protected void onPostExecute(List<City> data) {
            // Скрываем ProgressBar после возвращения результата
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
    }
}
