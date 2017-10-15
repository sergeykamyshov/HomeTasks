package ru.sergeykamyshov.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import ru.sergeykamyshov.schedule.adapters.StationRecyclerAdapter;
import ru.sergeykamyshov.schedule.models.City;
import ru.sergeykamyshov.schedule.utils.DBUtils;

public class StationListActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);

        setActionBarTitle(getString(R.string.action_bar_title_stations));

        // Настраиваем RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.stations_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Получаем данные из базы (или json файла если первый запуск)
        List<City> data = DBUtils.getStations(this);

        // Создаем и устанавливаем адаптер для RecyclerView
        recyclerView.setAdapter(new StationRecyclerAdapter(this, data));
    }
}
