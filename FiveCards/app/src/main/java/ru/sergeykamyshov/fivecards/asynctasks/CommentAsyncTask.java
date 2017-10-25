package ru.sergeykamyshov.fivecards.asynctasks;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import ru.sergeykamyshov.fivecards.adapters.CardViewAdapter;
import ru.sergeykamyshov.fivecards.model.CardType;
import ru.sergeykamyshov.fivecards.utils.QueryUtils;

/**
 * AsyncTask для загрузки конкретного id комментария
 */
public class CommentAsyncTask extends AsyncTask<String, Void, CardType> {

    private CardViewAdapter mAdapter;

    public CommentAsyncTask(CardViewAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    protected CardType doInBackground(String... params) {
        String commentNumber = params[0];
        return QueryUtils.fetchCardTypeData(QueryUtils.COMMENT_TYPE, commentNumber);
    }

    @Override
    protected void onPostExecute(CardType cardType) {
        // Удаляем старый тип карточки и добавляем новый
        List<CardType> newData = new ArrayList<>();
        newData.addAll(mAdapter.getData());
        newData.remove(1);
        newData.add(1, cardType);

        mAdapter.updateData(newData);
    }
}
