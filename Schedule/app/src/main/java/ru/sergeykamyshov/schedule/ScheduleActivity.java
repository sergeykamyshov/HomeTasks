package ru.sergeykamyshov.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ru.sergeykamyshov.schedule.fragments.DatePickerFragment;

public class ScheduleActivity extends BaseActivity {

    public static final String LOG_TAG = ScheduleActivity.class.getSimpleName();

    public static final int REQUEST_CODE_DEPARTURE = 1;
    public static final int REQUEST_CODE_ARRIVAL = 2;

    public static final String REQUEST_PARAM_STATION_NAME = "stationName";
    public static final String REQUEST_PARAM_STATION_REGION = "stationRegion";

    public static final String EXTRA_PARAM_DIRECTION_TYPE = "directionType";

    public static final String STATE_DEPARTURE_REGION = "departureStationRegion";
    public static final String STATE_ARRIVAL_REGION = "arrivalStationRegion";

    private TextView mDepartureStationRegion;
    private TextView mArrivalStationRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        setActionBarTitle(getString(R.string.action_bar_title_schedule));

        initViews();
    }

    private void initViews() {
        mDepartureStationRegion = (TextView) findViewById(R.id.departureStationRegionTextView);
        mArrivalStationRegion = (TextView) findViewById(R.id.arrivalStationRegionEditText);
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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mDepartureStationRegion.setText(savedInstanceState.getString(STATE_DEPARTURE_REGION));
        mArrivalStationRegion.setText(savedInstanceState.getString(STATE_ARRIVAL_REGION));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_DEPARTURE_REGION, String.valueOf(mDepartureStationRegion.getText()));
        outState.putString(STATE_ARRIVAL_REGION, String.valueOf(mArrivalStationRegion.getText()));
    }

    /**
     * Обрабатывает нажатие поля "Станция отправления" и вызывает экран списка станций
     *
     * @param view - компонент который был нажат
     */
    public void chooseDepartureStation(View view) {
        Intent intent = new Intent(this, StationListActivity.class);
        intent.putExtra(EXTRA_PARAM_DIRECTION_TYPE, "from");
        startActivityForResult(intent, REQUEST_CODE_DEPARTURE);
    }

    /**
     * Обрабатывает нажатие поля "Станция прибытия" и вызывает экран списка станций
     *
     * @param view - компонент который был нажат
     */
    public void chooseArrivalStation(View view) {
        Intent intent = new Intent(this, StationListActivity.class);
        intent.putExtra(EXTRA_PARAM_DIRECTION_TYPE, "to");
        startActivityForResult(intent, REQUEST_CODE_ARRIVAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_DEPARTURE:
                    EditText departureStation = (EditText) findViewById(R.id.departureStationAddressEditText);
                    departureStation.setText(data.getStringExtra(REQUEST_PARAM_STATION_NAME));
                    mDepartureStationRegion.setText(data.getStringExtra(REQUEST_PARAM_STATION_REGION));
                    break;
                case REQUEST_CODE_ARRIVAL:
                    EditText arrivalStation = (EditText) findViewById(R.id.arrivalStationAddressEditText);
                    arrivalStation.setText(data.getStringExtra(REQUEST_PARAM_STATION_NAME));
                    mArrivalStationRegion.setText(data.getStringExtra(REQUEST_PARAM_STATION_REGION));
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
