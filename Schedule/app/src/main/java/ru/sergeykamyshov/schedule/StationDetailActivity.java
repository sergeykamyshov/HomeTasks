package ru.sergeykamyshov.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

public class StationDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail);

        setActionBarTitle(getString(R.string.action_bar_title_station_detail));

        Intent intent = getIntent();

        TextView station = (TextView) findViewById(R.id.text_station);
        station.setText(intent.getStringExtra(PARAM_STATION_TITLE));

        TextView city = (TextView) findViewById(R.id.text_city);
        city.setText(intent.getStringExtra(PARAM_CITY_TITLE));

        // Регион необязательно поле, если оно не заполнено, то скрываем его на визуальной форме
        if (intent.getStringExtra(PARAM_DISTRICT_TITLE) != null) {
            TextView district = (TextView) findViewById(R.id.text_district);
            district.setText(intent.getStringExtra(PARAM_DISTRICT_TITLE));
        } else {
            TextView districtTitle = (TextView) findViewById(R.id.text_district_title);
            districtTitle.setVisibility(View.GONE);
            TextView district = (TextView) findViewById(R.id.text_district);
            district.setVisibility(View.GONE);
        }

        TextView country = (TextView) findViewById(R.id.text_country);
        country.setText(intent.getStringExtra(PARAM_COUNTRY_TITLE));
    }
}
