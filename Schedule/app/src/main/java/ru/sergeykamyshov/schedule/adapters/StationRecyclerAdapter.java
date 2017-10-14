package ru.sergeykamyshov.schedule.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ru.sergeykamyshov.schedule.R;
import ru.sergeykamyshov.schedule.models.City;
import ru.sergeykamyshov.schedule.models.Station;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        City city = mDataSet.get(position);
        // Формирование заголовка категории в формате: "Страна, Город"
        holder.mCountryCityTitle.setText(city.getCountryTitle() + ", " + city.getCityTitle());
        // Удаляем все станции для предыдущей категории, чтобы они накапливались
        holder.mStationContainerLayout.removeAllViews();
        for (Station station : city.getStations()) {
            View stationLayout = LayoutInflater.from(mContext).inflate(R.layout.station_list_item, holder.mStationContainerLayout, false);
            TextView stationTitle = stationLayout.findViewById(R.id.stationTitleTextView);
            stationTitle.setText(station.getStationTitle());

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
}
