package ru.sergeykamyshov.schedule.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ru.sergeykamyshov.schedule.R;
import ru.sergeykamyshov.schedule.ScheduleActivity;
import ru.sergeykamyshov.schedule.StationDetailActivity;
import ru.sergeykamyshov.schedule.models.City;
import ru.sergeykamyshov.schedule.models.Station;

import static ru.sergeykamyshov.schedule.BaseActivity.PARAM_CITY_TITLE;
import static ru.sergeykamyshov.schedule.BaseActivity.PARAM_COUNTRY_TITLE;
import static ru.sergeykamyshov.schedule.BaseActivity.PARAM_DISTRICT_TITLE;
import static ru.sergeykamyshov.schedule.BaseActivity.PARAM_STATION_TITLE;
import static ru.sergeykamyshov.schedule.ScheduleActivity.REQUEST_PARAM_STATION_NAME;
import static ru.sergeykamyshov.schedule.ScheduleActivity.REQUEST_PARAM_STATION_REGION;

public class StationRecyclerAdapter extends RecyclerView.Adapter<StationRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<City> mDataSet;

    public StationRecyclerAdapter(Context context, List<City> dataSet) {
        mContext = context;
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.country_city_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final City city = mDataSet.get(position);
        // Формирование заголовка категории в формате: "Страна, Город"
        final String countryCityTitle = city.getCountryTitle() + ", " + city.getCityTitle();
        holder.mCountryCityTitle.setText(countryCityTitle);
        // Удаляем все станции для предыдущей категории, чтобы они не накапливались
        holder.mStationContainerLayout.removeAllViews();
        for (final Station station : city.getStations()) {
            View stationLayout = LayoutInflater.from(mContext).inflate(R.layout.station_list_item, holder.mStationContainerLayout, false);
            final TextView stationTitle = stationLayout.findViewById(R.id.stationTitleTextView);
            stationTitle.setText(station.getStationTitle());

            ImageView moreInfoImage = stationLayout.findViewById(R.id.stationMoreInfoImageView);
            moreInfoImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, StationDetailActivity.class);
                    intent.putExtra(PARAM_STATION_TITLE, station.getStationTitle());
                    intent.putExtra(PARAM_CITY_TITLE, station.getFullCityTitle() != null ? station.getFullCityTitle() : city.getCityTitle());
                    intent.putExtra(PARAM_DISTRICT_TITLE, station.getDistrictTitle());
                    intent.putExtra(PARAM_COUNTRY_TITLE, city.getCountryTitle());
                    mContext.startActivity(intent);
                }
            });

            stationLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = (Activity) mContext;
                    Intent resultIntent = new Intent(activity, ScheduleActivity.class);
                    resultIntent.putExtra(REQUEST_PARAM_STATION_NAME, station.getStationTitle());
                    resultIntent.putExtra(REQUEST_PARAM_STATION_REGION, countryCityTitle);
                    activity.setResult(Activity.RESULT_OK, resultIntent);
                    activity.finish();
                }
            });

            holder.mStationContainerLayout.addView(stationLayout);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mCountryCityTitle;
        public LinearLayout mStationContainerLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mCountryCityTitle = itemView.findViewById(R.id.countryCityTitleTextView);
            mStationContainerLayout = itemView.findViewById(R.id.stationsContainerLayout);
        }
    }

    public void setDataSet(List<City> dataSet) {
        mDataSet = dataSet;
    }
}
