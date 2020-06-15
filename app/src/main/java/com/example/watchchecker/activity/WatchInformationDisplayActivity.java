package com.example.watchchecker.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.watchchecker.R;
import com.example.watchchecker.data.TimekeepingEntry;
import com.example.watchchecker.data.TimingEntry;
import com.example.watchchecker.data.UserData;
import com.example.watchchecker.data.WatchDataEntry;
import com.example.watchchecker.util.Timekeeping_Util;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * An {@link Activity} used to display all of the information associated with a {@link WatchDataEntry},
 * including {@link TimekeepingEntry} info
 */
public class WatchInformationDisplayActivity extends AppCompatActivity implements Observer {

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
        setTimekeepingLinearLayout(watchDataEntry);
        // Set FAB to check your watch against the reference time
        setTimeWatchButton(watchDataEntry);
        UserData.getWatchTimekeepingMap().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        setDeviationTextView(watchDataEntry);
        setTimekeepingLinearLayout(watchDataEntry);
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
        // Display accuracy of watch
        setDeviationTextView(watchDataEntry);
    }

    private void setDeviationTextView(WatchDataEntry watchDataEntry) {
        TextView accuracyDisplayTextView = findViewById(R.id.display_watch_average_deviation_text);
        accuracyDisplayTextView.append(Timekeeping_Util.calculateAverageDeviation(watchDataEntry).toFullDisplayString());
    }

    private void setTimekeepingLinearLayout(WatchDataEntry watchDataEntry) {
        LinearLayoutCompat timekeepingLinearLayout = findViewById(R.id.display_watch_timekeeping_entry_list_view);
        timekeepingLinearLayout.removeAllViews();
        List<TimekeepingEntry> timekeepingEntries = UserData.getTimekeepingEntries(watchDataEntry);
        // Inflate layout for each timekeeping entry
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        addTimekeepingEntryHeaderCard(inflater, timekeepingLinearLayout);
        for (int i = 0; i < timekeepingEntries.size(); i++) {
            View timekeepingEntryView = inflater.inflate(R.layout.timekeeping_entry_card, timekeepingLinearLayout, false);
            // Set text in textview to show date range in entry
            TextView dateRangeTextView = timekeepingEntryView.findViewById(R.id.timekeeping_entry_date_range_text_view);
            dateRangeTextView.setText(timekeepingEntries.get(i).getDateStringRange().toDisplayString());
            // Set text in textview to show timing deviation in entry
            TextView deviationTextView = timekeepingEntryView.findViewById(R.id.timekeeping_entry_deviation_text_view);
            deviationTextView.setText(timekeepingEntries.get(i).getTimingDeviation().toFullDisplayString());
            // Set detail view which will be displayed after tapping a timekeeping run card view
            LinearLayout detailViewLinearLayout = timekeepingEntryView.findViewById(R.id.timekeeping_entry_detail_view);
            detailViewLinearLayout.removeAllViews();
            List<TimingEntry> timingEntries = timekeepingEntries.get(i).getTimingEntries();
            addTimingEntryHeaderCard(inflater, detailViewLinearLayout);
            for (int j = 0; j < timingEntries.size(); j++) {
                View timingEntryView = inflater.inflate(R.layout.timing_entry_card, detailViewLinearLayout, false);
                // Display reference time of timing entry
                TextView referenceTimeTextView = timingEntryView.findViewById(R.id.timing_entry_reference_time_text_view);
                referenceTimeTextView.setText(timingEntries.get(j).getReferenceDateString().getComplexDateString());
                // Display deviation in timing entry
                TextView detailDeviationTextView = timingEntryView.findViewById(R.id.timing_entry_watch_deviation_text_view);
                detailDeviationTextView.setText(timingEntries.get(j).getDeviation().toSimpleDisplayString());
                // Add view to parent
                detailViewLinearLayout.addView(timingEntryView);
            }
            // Set onclick listener to display the detail view
            CardView timekeepingEntryCardView = timekeepingEntryView.findViewById(R.id.timekeeping_entry_card_view);
            timekeepingEntryCardView.setOnClickListener(v -> {
                TransitionManager.beginDelayedTransition(timekeepingEntryCardView);
                if (detailViewLinearLayout.getVisibility() == View.GONE) {
                    detailViewLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    detailViewLinearLayout.setVisibility(View.GONE);
                }
            });
            // All done, add it to the view
            timekeepingLinearLayout.addView(timekeepingEntryView);
        }
    }

    private void addTimingEntryHeaderCard(LayoutInflater inflater, LinearLayout timingEntryLinearLayout) {
        View headerView = inflater.inflate(R.layout.timing_entry_card, timingEntryLinearLayout, false);
        MaterialCardView headerCardView = headerView.findViewById(R.id.timing_entry_card_view);
        headerCardView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        timingEntryLinearLayout.addView(headerView);
    }

    private void addTimekeepingEntryHeaderCard(LayoutInflater inflater, LinearLayoutCompat timekeepingLinearLayout) {
        View headerView = inflater.inflate(R.layout.timekeeping_entry_card, timekeepingLinearLayout, false);
        // Set colour since this is a header card
        MaterialCardView headerCardView = headerView.findViewById(R.id.timekeeping_entry_card_view);
        headerCardView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        // Add to the linear layout view
        timekeepingLinearLayout.addView(headerView);
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

    /**
     * Set up the {@link Button} that initiates {@link TimeWatchActivity} and thus a timekeeping
     * event
     */
    private void setTimeWatchButton(WatchDataEntry watchDataEntry) {
        FloatingActionButton fab = findViewById(R.id.display_watch_start_timing_fab);
        fab.setOnClickListener(v -> {
            // Setup and start activity to display timekeeping information
            Intent intent = new Intent(getApplicationContext(), TimeWatchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(WatchDataEntry.PARCEL_KEY, watchDataEntry);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}
