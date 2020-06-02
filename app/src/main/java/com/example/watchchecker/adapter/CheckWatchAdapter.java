package com.example.watchchecker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.watchchecker.R;
import com.example.watchchecker.dataModel.WatchDataEntry;

import java.util.List;

public class CheckWatchAdapter extends BaseAdapter {

    private final Context context;

    /**
     * The list of {@link WatchDataEntry} objects that form each element of the GridView
     */
    private final List<WatchDataEntry> watchDataEntries;

    public CheckWatchAdapter(Context context, List<WatchDataEntry> watchDataEntries) {
        this.context = context;
        this.watchDataEntries = watchDataEntries;
    }

    @Override
    public int getCount() {
        return watchDataEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return watchDataEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView watchDialImageView = new ImageView(context);
        watchDialImageView.setImageResource(R.drawable.ic_check_watch_table_view_diver_filled);
        watchDialImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        watchDialImageView.setLayoutParams(new GridView.LayoutParams(70,70));
        return watchDialImageView;
    }
}
