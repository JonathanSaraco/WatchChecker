package com.example.watchchecker.fragment;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.example.watchchecker.R;
import com.example.watchchecker.activity.AddWatchActivity;
import com.example.watchchecker.adapter.WatchCollectionAdapter;
import com.example.watchchecker.data.UserData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class WatchCollectionFragment extends Fragment {

    public WatchCollectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_watch_collection, container, false);
        // Add the gridView to display WatchDataEntries
        GridView gridView = fragmentView.findViewById(R.id.gridView_check_watch);
        WatchCollectionAdapter watchCollectionAdapter = new WatchCollectionAdapter(fragmentView.getContext(), UserData.getWatchDataEntries());
        gridView.setAdapter(watchCollectionAdapter);
        // Add the floating action button for adding a new watch data entry
        FloatingActionButton fab = fragmentView.findViewById(R.id.fab); //Get the floating action button view
        fab.setImageResource(R.drawable.ic_plus_one); //Set button icon to a plus one icon
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark))); //Set the background colour of the fab
        fab.setOnClickListener(view -> {
            Intent in = new Intent(getActivity(), AddWatchActivity.class);
            startActivity(in);
            WatchCollectionAdapter newWatchCollectionAdapter = new WatchCollectionAdapter(fragmentView.getContext(), UserData.getWatchDataEntries());
            gridView.setAdapter(newWatchCollectionAdapter);
        });
        return fragmentView;
    }

}
