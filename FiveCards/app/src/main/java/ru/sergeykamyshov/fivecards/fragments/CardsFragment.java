package ru.sergeykamyshov.fivecards.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.sergeykamyshov.fivecards.R;

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
        return inflater.inflate(R.layout.fragment_cards, container, false);
    }
}
