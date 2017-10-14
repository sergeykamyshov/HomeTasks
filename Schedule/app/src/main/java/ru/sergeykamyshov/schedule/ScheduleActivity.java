package ru.sergeykamyshov.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ru.sergeykamyshov.schedule.fragments.DatePickerFragment;

public class ScheduleActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    public static final String LOG_TAG = ScheduleActivity.class.getSimpleName();
    public static final String REQUEST_PARAM_STATION_NAME = "stationName";
    public static final String REQUEST_PARAM_STATION_REGION = "stationRegion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.action_bar_title_schedule));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(getString(R.string.menu_item_about));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Обрабатывает нажатие поля "Станция отправления" и вызывает экран списка станций
     *
     * @param view - компонент который был нажат
     */
    public void chooseDepartureStation(View view) {
        Intent intent = new Intent(this, StationListActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    EditText departureStation = (EditText) findViewById(R.id.departureStationAddressEditText);
                    departureStation.setText(data.getStringExtra(REQUEST_PARAM_STATION_NAME));
                    TextView departureStationRegion = (TextView) findViewById(R.id.departureStationRegionTextView);
                    departureStationRegion.setText(data.getStringExtra(REQUEST_PARAM_STATION_REGION));
                    break;
                default:
                    Log.i(LOG_TAG, getString(R.string.error_request_code_not_identified) + requestCode);
            }
        }
    }

    /**
     * Обрабатывает нажатие поля "Дата отправления" и показывает диалог выбора даты
     *
     * @param view - компонент который был нажат
     */
    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
