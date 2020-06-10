package com.example.watchchecker.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.watchchecker.R;
import com.example.watchchecker.adapter.TimekeepingEntryAdapter;
import com.example.watchchecker.data.TimekeepingEntry;
import com.example.watchchecker.data.UserData;
import com.example.watchchecker.data.WatchDataEntry;

/**
 * An {@link Activity} used to display all of the information associated with a {@link WatchDataEntry},
 * including {@link TimekeepingEntry} info
 */
public class WatchInformationDisplayActivity extends AppCompatActivity {

    private WatchDataEntry watchDataEntry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_watch_info);
        // Unparcel the WatchDataEntry that caused all this
        watchDataEntry = getIntent().getExtras().getParcelable(WatchDataEntry.PARCEL_KEY);
        // Set text view components
        setTextViewComponents(watchDataEntry);
        // Set timekeeping entry grid view
        setTimekeepingGridView(watchDataEntry);
    }

    private void setTextViewComponents(WatchDataEntry watchDataEntry) {
        // Get image view that displays the watch and set its drawable
        ImageView watchImageView = findViewById(R.id.display_watch_image);
        watchImageView.setImageResource(R.drawable.watch_placeholder_image);
        watchImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        // Set the text views to display watchDataEntry information
        setSimpleTextViewText(R.id.display_watch_identifier_text, watchDataEntry.toDisplayString());
        setComplexTextViewText(R.id.display_watch_movement_text, watchDataEntry.getMovement());
        setComplexTextViewText(R.id.display_watch_purchase_date_text, watchDataEntry.getPurchaseDate().getSimpleDateString());
        setComplexTextViewText(R.id.display_watch_service_date_text, watchDataEntry.getLastServiceDate().getSimpleDateString());
    }

    private void setTimekeepingGridView(WatchDataEntry watchDataEntry) {
        ListView timekeepingGridView = findViewById(R.id.display_watch_timekeeping_entry_grid_view);
        TimekeepingEntryAdapter timekeepingEntryAdapter = new TimekeepingEntryAdapter(getApplicationContext(), UserData.getTimekeepingEntries(watchDataEntry));
        timekeepingGridView.setAdapter(timekeepingEntryAdapter);
    }

    private void setSimpleTextViewText(int resourceID, String text) {
        TextView textView = findViewById(resourceID);
        if (!text.isEmpty()) {
            textView.setText(text);
        } else {
            textView.setHeight(0);
        }
    }

    private void setComplexTextViewText(int resourceID, String secondText) {
        TextView textView = findViewById(resourceID);
        if (!secondText.isEmpty()) {
            // Set text part one, will use the text specified in the layout.xml, but give it gray text
            Spannable textPartOne = new SpannableString(textView.getText());
            textPartOne.setSpan(new ForegroundColorSpan(Color.GRAY), 0, textPartOne.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(textPartOne);
            // Set part two, will use the text we want, but in black because it's the "value" of what
            // we're displaying
            Spannable textPartTwo = new SpannableString(secondText);
            textPartTwo.setSpan(new ForegroundColorSpan(Color.BLACK), 0, textPartTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.append(textPartTwo);
        } else {
            textView.setHeight(0);
        }
    }
}
