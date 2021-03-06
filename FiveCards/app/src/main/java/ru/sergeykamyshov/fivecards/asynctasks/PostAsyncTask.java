package ru.sergeykamyshov.fivecards.asynctasks;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import ru.sergeykamyshov.fivecards.adapters.CardViewAdapter;
import ru.sergeykamyshov.fivecards.model.CardType;
import ru.sergeykamyshov.fivecards.utils.QueryUtils;

/**
 * AsyncTask для загрузки конкретного id поста
 */
public class PostAsyncTask extends AsyncTask<String, Void, CardType> {

    private CardViewAdapter mAdapter;

    public PostAsyncTask(CardViewAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    protected CardType doInBackground(String... params) {
        String postNumber = params[0];
        return QueryUtils.fetchCardTypeData(QueryUtils.POST_TYPE, postNumber);
    }

    @Override
    protected void onPostExecute(CardType cardType) {
        // Удаляем старый тип карточки и добавляем новый
        List<CardType> newData = new ArrayList<>();
        newData.addAll(mAdapter.getData());
        newData.remove(0);
        newData.add(0, cardType);

        mAdapter.updateData(newData);
    }
}
