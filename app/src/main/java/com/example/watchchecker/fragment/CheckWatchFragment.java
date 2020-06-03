package com.example.watchchecker.fragment;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.watchchecker.R;
import com.example.watchchecker.activity.CheckWatchActivity;
import com.example.watchchecker.adapter.CheckWatchAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckWatchFragment extends Fragment {

    public CheckWatchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_check_watch, container, false);



        // Add the gridView
        GridView gridView = fragmentView.findViewById(R.id.gridView_check_watch);
        CheckWatchAdapter checkWatchAdapter = new CheckWatchAdapter(fragmentView.getContext(), new ArrayList<>());
        gridView.setAdapter(checkWatchAdapter);

        // Add the floating action button
        FloatingActionButton fab = fragmentView.findViewById(R.id.fab); //Get the floating action button view
        fab.setImageResource(R.drawable.ic_plus_one); //Set button icon to a plus one icon
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark))); //Set the background colour of the fab
        fab.setOnClickListener(view -> {
            Intent in = new Intent(getActivity(), CheckWatchActivity.class);
            startActivity(in);
        });

        return fragmentView;
    }

}
