package ru.sergeykamyshov.imgurclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.sergeykamyshov.imgurclient.R;
import ru.sergeykamyshov.imgurclient.adapters.ImagesRecyclerAdapter;

public class ImagesFragment extends Fragment {

    public static final int SPAN_COUNT = 2;

    public static ImagesFragment newInstance() {
        return new ImagesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_images);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL));

        ImagesRecyclerAdapter recyclerAdapter = new ImagesRecyclerAdapter(getContext());
        recyclerView.setAdapter(recyclerAdapter);

        return view;
    }
}
