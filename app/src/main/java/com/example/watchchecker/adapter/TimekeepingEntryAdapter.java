package com.example.watchchecker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.watchchecker.R;
import com.example.watchchecker.activity.WatchInformationDisplayActivity;
import com.example.watchchecker.data.TimekeepingEntry;

import java.util.List;

/**
 * An {@link Adapter} for the {@link GridView} displayed as a part of
 * {@link WatchInformationDisplayActivity}.
 */
public class TimekeepingEntryAdapter extends BaseAdapter {

    private final Context context;

    /**
     * The list of {@link TimekeepingEntry}s that form the elements of the grid view
     */
    private final List<TimekeepingEntry> timekeepingEntries;

    public TimekeepingEntryAdapter(Context context, List<TimekeepingEntry> timekeepingEntries) {
        this.context = context;
        this.timekeepingEntries = timekeepingEntries;
    }

    @Override
    public int getCount() {
        return timekeepingEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return timekeepingEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the timekeeping entry that we will display data for in the view
        TimekeepingEntry timingKeepingEntry = timekeepingEntries.get(position);
        // Set up view if it is null
        if (convertView == null) {
            // Inflate timekeeping entry view
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.timekeeping_entry_card, parent, false);
            // Set text in textview to show timing deviation in entry
            TextView textView = convertView.findViewById(R.id.timekeeping_entry_date_range_text_view);
            textView.setText(timingKeepingEntry.getTimingDeviation().toDisplayString());
        }
        return convertView;
    }
}
