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
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import ru.sergeykamyshov.fivecards.R;
import ru.sergeykamyshov.fivecards.adapters.CardViewAdapter;
import ru.sergeykamyshov.fivecards.model.CardType;
import ru.sergeykamyshov.fivecards.utils.QueryUtils;

import static ru.sergeykamyshov.fivecards.utils.QueryUtils.COMMENT_TYPE;
import static ru.sergeykamyshov.fivecards.utils.QueryUtils.IMAGE_TYPE;
import static ru.sergeykamyshov.fivecards.utils.QueryUtils.POST_TYPE;
import static ru.sergeykamyshov.fivecards.utils.QueryUtils.TODO_TYPE;
import static ru.sergeykamyshov.fivecards.utils.QueryUtils.USERS_TYPE;

/**
 * Фрагмент отвечает за обработку экрана списка из 5 карточек
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

        public static final String DEFAULT_POST_ID = "1";
        public static final String DEFAULT_COMMENT_ID = "1";

        @Override
        protected List<CardType> doInBackground(Void... params) {
            List<CardType> data = new ArrayList<>();

            List<CardType> postTypes = QueryUtils.fetchCardTypeData(POST_TYPE, DEFAULT_POST_ID);
            data.add(postTypes.get(0));

            List<CardType> commentTypes = QueryUtils.fetchCardTypeData(COMMENT_TYPE, DEFAULT_COMMENT_ID);
            data.add(commentTypes.get(0));

            List<CardType> usersTypes = QueryUtils.fetchCardTypeData(USERS_TYPE);
            data.add(usersTypes.get(0));

            List<CardType> imageTypes = QueryUtils.fetchCardTypeData(IMAGE_TYPE);
            data.add(imageTypes.get(0));

            List<CardType> todoTypes = QueryUtils.fetchCardTypeData(TODO_TYPE);
            data.add(todoTypes.get(0));

            return data;
        }

        @Override
        protected void onPostExecute(List<CardType> cardTypes) {
            ProgressBar progressBar = getActivity().findViewById(R.id.load_progress);
            progressBar.setVisibility(View.GONE);

            mCardViewAdapter.updateData(cardTypes);
        }
    }

}
