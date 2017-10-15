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

import java.util.ArrayList;
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
    /**
     * Список данных используемый адаптером для отображения списка
     */
    private List<City> mDataSet;
    /**
     * Всегда полный список данных используемый для поиска данных по запросу
     */
    private List<City> mFullDataSet;

    public StationRecyclerAdapter(Context context, List<City> dataSet) {
        mContext = context;
        setDataSet(dataSet);
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
        // Удаляем все станции для предыдущей категории, чтобы не накапливались
        holder.mStationContainerLayout.removeAllViews();
        for (final Station station : city.getStations()) {
            // Т.к. у городов разное кол-вол станций, то создаем и заполняем каждый список станций программно в цикле
            View stationLayout = LayoutInflater.from(mContext).inflate(R.layout.station_list_item, holder.mStationContainerLayout, false);
            final TextView stationTitle = stationLayout.findViewById(R.id.stationTitleTextView);
            stationTitle.setText(station.getStationTitle());

            // Нажатие на иконку "Подробнее" должно открывать новый экран с полной информацией
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

            // Нажатие на конкретную станцию должно заполнять поле на экране "Расписание" текущим значением
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

    /**
     * Производит фильтрацию данных адаптера по переданому запросу
     *
     * @param query - запрос поиска
     */
    public void filterByQuery(String query) {
        if (query.isEmpty()) {
            // Если пользователь полностью очистил запрос в поиске, то показать полный список
            mDataSet = mFullDataSet;
        } else {
            List<City> filteredCities = new ArrayList<>();
            // Поиск всегда осуществляется по полному списку
            for (City city : mFullDataSet) {
                // Искать необходимо с игнорированием регистра, поэтому приводим все к нижнему регистру
                String lowerCaseQuery = query.toLowerCase();
                // Возможен поиск по городу или стране, мало ли
                if (city.getCountryTitle().toLowerCase().contains(lowerCaseQuery) || city.getCityTitle().toLowerCase().contains(lowerCaseQuery)) {
                    // Если есть совпадение по городу/стране, то сразу добавляем весь город в список
                    filteredCities.add(city);
                    continue;
                }
                List<Station> filteredStations = new ArrayList<>();
                // Если по городу/стране не нашли, надо искать по отдельным станциям
                for (Station station : city.getStations()) {
                    if (station.getStationTitle().toLowerCase().contains(lowerCaseQuery)) {
                        // Если станция подошла, то добавляем ее в новый список - отсортированных станций
                        filteredStations.add(station);
                    }
                }
                // Список должен быть заполнен, если нашли подходящие станции
                if (!filteredStations.isEmpty()) {
                    // Создаем новый город со списком только подходящих станций
                    City cityWithFilteredStations = new City(city.getCountryTitle(), city.getCityTitle(), filteredStations);
                    filteredCities.add(cityWithFilteredStations);
                }
            }
            // Устанавливаем адаптера новый список данных для отображения
            mDataSet = filteredCities;
        }
        notifyDataSetChanged();
    }

    /**
     * Помещает данные в два списка, т.к. один из них всегда должен содержать полную информацию (mFullDataSet),
     * а другой отфильтрованные данные (mDataSet)
     *
     * @param dataSet - список с данными
     */
    public void setDataSet(List<City> dataSet) {
        mFullDataSet = dataSet;
        mDataSet = dataSet;
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
