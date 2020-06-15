package com.example.watchchecker.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.watchchecker.R;
import com.example.watchchecker.activity.WatchInformationDisplayActivity;
import com.example.watchchecker.data.UserData;
import com.example.watchchecker.data.WatchDataEntry;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class WatchCollectionAdapter extends BaseAdapter implements Observer {

    private final Context context;

    /**
     * The list of {@link WatchDataEntry} objects that form each element of the GridView
     */
    private List<WatchDataEntry> watchDataEntries;

    public WatchCollectionAdapter(Context context, List<WatchDataEntry> watchDataEntries) {
        this.context = context;
        this.watchDataEntries = watchDataEntries;
        UserData.getWatchTimekeepingMap().addObserver(this);
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
        WatchDataEntry watchDataEntry = watchDataEntries.get(position);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.watch_collection_grid_view_element, null);
            // Setup on-click listener to display this watch entry's information
            convertView.setOnClickListener(v -> {
                // Setup and start activity to display timekeeping information
                Intent intent = new Intent(context, WatchInformationDisplayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(WatchDataEntry.PARCEL_KEY, watchDataEntry);
                intent.putExtras(bundle);
                context.startActivity(intent);
            });
            // Text view to display watch brand and model
            TextView watchDisplayTextView = convertView.findViewById(R.id.display_watch_element_name_text_view);
            watchDisplayTextView.setText(watchDataEntry.toDisplayString());
            // TextView to display movement name
            TextView watchMovementTextView = convertView.findViewById(R.id.display_watch_element_movement_text_view);
            watchMovementTextView.setText(watchDataEntry.getMovement());
            // Image view to display image associated with watch
            ImageView watchDialImageView = convertView.findViewById(R.id.display_watch_element_image_view);
            watchDialImageView.setImageResource(R.drawable.watch_placeholder_image);
            // Set on-click event of settings button
            ImageButton settingsButton = convertView.findViewById(R.id.watch_collection_element_settings_button);
            settingsButton.setOnClickListener(v -> {
                // Instantiate and inflate popup menu
                PopupMenu popupMenu = new PopupMenu(context, settingsButton);
                popupMenu.getMenuInflater().inflate(R.menu.watch_collection_element_settings_menu, popupMenu.getMenu());
                // Set menu item onclick listener behaviour
                popupMenu.setOnMenuItemClickListener(item -> {
                    Toast.makeText(
                            context,
                            "You Clicked : " + item.getTitle(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return true;
                });
                popupMenu.show();
            });
        }
        return convertView;
    }

    @Override
    public void update(Observable o, Object arg) {
        watchDataEntries = UserData.getWatchDataEntries();
        notifyDataSetChanged();
    }

    private void setCommonTextViewProperties(TextView textView) {
        textView.setPadding(context.getResources().getDimensionPixelSize(R.dimen.watch_collection_text_padding_left),
                textView.getTotalPaddingTop(),
                textView.getPaddingRight(),
                textView.getPaddingBottom());
        textView.setHorizontallyScrolling(true);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setGravity(Gravity.LEFT);
    }
}
