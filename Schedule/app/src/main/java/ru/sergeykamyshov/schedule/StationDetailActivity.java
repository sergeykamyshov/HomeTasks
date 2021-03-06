package ru.sergeykamyshov.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

/**
 * Класс отвечает за обработку на экране "Подробно о станции"
 */
public class StationDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail);

        setActionBarTitle(getString(R.string.action_bar_title_station_detail));
        enableToolbarBackButton();

        Intent intent = getIntent();

        TextView station = (TextView) findViewById(R.id.text_station);
        station.setText(intent.getStringExtra(PARAM_STATION_TITLE));

        TextView city = (TextView) findViewById(R.id.text_city);
        city.setText(intent.getStringExtra(PARAM_CITY_TITLE));

        // Регион необязательно поле, если оно не заполнено, то скрываем его на визуальной форме
        if (intent.getStringExtra(PARAM_DISTRICT_TITLE) == null || intent.getStringExtra(PARAM_DISTRICT_TITLE).isEmpty()) {
            TextView districtTitle = (TextView) findViewById(R.id.text_district_title);
            districtTitle.setVisibility(View.GONE);
            TextView district = (TextView) findViewById(R.id.text_district);
            district.setVisibility(View.GONE);
        } else {
            TextView district = (TextView) findViewById(R.id.text_district);
            district.setText(intent.getStringExtra(PARAM_DISTRICT_TITLE));
        }

        TextView country = (TextView) findViewById(R.id.text_country);
        country.setText(intent.getStringExtra(PARAM_COUNTRY_TITLE));
    }
}
