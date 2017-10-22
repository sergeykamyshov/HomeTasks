package ru.sergeykamyshov.fivecards.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.sergeykamyshov.fivecards.R;
import ru.sergeykamyshov.fivecards.adapters.CardViewAdapter;
import ru.sergeykamyshov.fivecards.model.CardType;
import ru.sergeykamyshov.fivecards.model.CommentType;
import ru.sergeykamyshov.fivecards.utils.QueryUtils;

import static ru.sergeykamyshov.fivecards.utils.QueryUtils.POST_TYPE;

/**
 * Фрагмент отвечает за обработку экрана списка 5 карточек
 */
public class CardsFragment extends Fragment {

    private CardViewAdapter mCardViewAdapter;

    public static CardsFragment newInstance() {
        return new CardsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cards, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.cards_recycler_veiw);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCardViewAdapter = new CardViewAdapter(getContext(), new ArrayList<CardType>());
        recyclerView.setAdapter(mCardViewAdapter);

        new CardsAsyncTask().execute();

        return view;
    }

    class CardsAsyncTask extends AsyncTask<Void, Void, List<CardType>> {
        @Override
        protected List<CardType> doInBackground(Void... params) {
            List<CardType> data = new ArrayList<>();

            List<CardType> postTypes = QueryUtils.fetchCardTypeData(POST_TYPE);
            data.add(postTypes.get(0));

            // TODO: удалить заглушку
            data.add(new CommentType(0, "id labore ex et quam laborum", "Eliseo@gardner.biz", "laudantium enim quasi est quidem magnam voluptate ipsam eostempora quo necessitatibus dolor quam autem quasi reiciendis et nam sapiente accusantium"));

            return data;
        }

        @Override
        protected void onPostExecute(List<CardType> cardTypes) {
            mCardViewAdapter.updateData(cardTypes);
        }
    }

}
