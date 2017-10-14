package ru.sergeykamyshov.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import ru.sergeykamyshov.schedule.adapters.StationRecyclerAdapter;
import ru.sergeykamyshov.schedule.models.City;
import ru.sergeykamyshov.schedule.utils.QueryUtils;

public class StationListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.action_bar_title_stations));
        }

        // Настраиваем RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.stations_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Получаем данные из json файла
        List<City> data = QueryUtils.fetchStationDataFromAssetsFile(this);

        // Создаем и устанавливаем адаптер для RecyclerView
        recyclerView.setAdapter(new StationRecyclerAdapter(this, data));
    }
}
