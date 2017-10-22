package ru.sergeykamyshov.fivecards.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.sergeykamyshov.fivecards.BuildConfig;
import ru.sergeykamyshov.fivecards.R;

/**
 * Фрагмент отвечает за обработку экрана "О приложении"
 */
public class AboutFragment extends Fragment {

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView versionTextView = view.findViewById(R.id.text_version);
        versionTextView.append(String.valueOf(BuildConfig.VERSION_NAME));

        return view;
    }
}
