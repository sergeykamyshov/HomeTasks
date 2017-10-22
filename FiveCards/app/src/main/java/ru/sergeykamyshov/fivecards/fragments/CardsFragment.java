package ru.sergeykamyshov.fivecards.fragments;

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
import ru.sergeykamyshov.fivecards.model.PostType;

/**
 * Фрагмент отвечает за обработку экрана списка 5 карточек
 */
public class CardsFragment extends Fragment {

    public static CardsFragment newInstance() {
        return new CardsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cards, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.cards_recycler_veiw);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<CardType> data = generateTestTypeData();
        recyclerView.setAdapter(new CardViewAdapter(getContext(), data));

        return view;
    }

    private List<CardType> generateTestTypeData() {
        List<CardType> titles = new ArrayList<>();
        titles.add(new PostType(0, "Test Post Title", "Test Post Body"));
        return titles;
    }
}
