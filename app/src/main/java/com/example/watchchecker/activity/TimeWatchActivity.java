package com.example.watchchecker.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.watchchecker.R;
import com.example.watchchecker.data.TimekeepingEntry;
import com.example.watchchecker.data.TimingDeviation;
import com.example.watchchecker.data.TimingEntry;
import com.example.watchchecker.data.UserData;
import com.example.watchchecker.data.WatchDataEntry;
import com.example.watchchecker.util.ThemeUtil;
import com.example.watchchecker.util.Timekeeping_Util;
import com.ikovac.timepickerwithseconds.TimePicker;

import org.apache.commons.lang3.time.DateUtils;

import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * An {@link Activity} which adds a {@link TimingEntry} to a {@link TimekeepingEntry}.
 */
public class TimeWatchActivity extends AppCompatActivity {

    WatchDataEntry watchDataEntry;

    Handler handler = new Handler();
    Runnable runnable = () -> {};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeUtil.getNoActionBarThemeResourceID(ThemeUtil.getThemeFromPreferences(getApplicationContext())));
        setContentView(R.layout.activity_time_watch);
        // Unparcel the WatchDataEntry that this timekeeping event is for
        watchDataEntry = getIntent().getExtras().getParcelable(WatchDataEntry.PARCEL_KEY);
        // Set TimePicker to round up to the next minute
        TimePicker timePicker = findViewById(R.id.time_watch_time_picker);
        roundUpMinuteOfTimePicker(timePicker);
        // Get deviation text view being displayed and updated
        TextView deviationTextView = findViewById(R.id.time_watch_deviation_text_view);
        setDeviationUpdateRunnable(deviationTextView, timePicker);
        // Get new timekeeping run switch to make a new timekeeping entry upon timing
        Switch newRunSwitch = findViewById(R.id.time_watch_new_timekeeping_run_switch);
        // Set measure deviation card view as a button that will add a new timekeeping entry
        CardView measureDeviationCardView = findViewById(R.id.time_watch_card_view_measure_deviation);
        measureDeviationCardView.setOnClickListener(v -> {
            // Get reference Date and Date corresponding to what the timePicker is showing
            Calendar referenceCalendar = Calendar.getInstance();
            Calendar timePickerCalendar = new GregorianCalendar(referenceCalendar.get(Calendar.YEAR),
                    referenceCalendar.get(Calendar.MONTH),
                    referenceCalendar.get(Calendar.DATE),
                    timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute(),
                    timePicker.getCurrentSeconds());
            // Add new data to the map
            UserData.addTimingEntry(watchDataEntry, new TimingEntry(timePickerCalendar.getTime(), referenceCalendar.getTime()), newRunSwitch.isChecked());
            // Send vibration
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            int vibrateTime = 100;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(vibrateTime, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrator.vibrate(vibrateTime);
            }
            // Finish activity
            this.finish();
        });
        handler.post(runnable);
    }

    private void roundUpMinuteOfTimePicker(TimePicker timePicker) {
        Date currentTime = Timekeeping_Util.getReferenceTime();
        currentTime = DateUtils.truncate(currentTime, Calendar.MINUTE);
        currentTime = new Date(currentTime.getTime() + 1_000*60);
        timePicker.setCurrentHour(currentTime.getHours());
        timePicker.setCurrentMinute(currentTime.getMinutes());
        timePicker.setCurrentSecond(0);
    }

    private void setDeviationUpdateRunnable(TextView deviationTextView, TimePicker timePicker) {
        runnable = () -> {
            TimingDeviation timingDeviation = getTimePickerDeviation(timePicker);
            // Set textView
            deviationTextView.setText(String.format("%s %s", timingDeviation.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toPlainString(), "s"));
            // Repeat every 10 milliseconds
            handler.postDelayed(runnable, 10);
        };
    }

    private TimingDeviation getTimePickerDeviation(TimePicker timePicker) {
        // Get reference Date and Date corresponding to what the timePicker is showing
        Calendar referenceCalendar = Calendar.getInstance();
        Calendar timePickerCalendar = new GregorianCalendar(referenceCalendar.get(Calendar.YEAR),
                referenceCalendar.get(Calendar.MONTH),
                referenceCalendar.get(Calendar.DATE),
                timePicker.getCurrentHour(),
                timePicker.getCurrentMinute(),
                timePicker.getCurrentSeconds());
        // Get deviation
        return Timekeeping_Util.calculateDeviation(referenceCalendar.getTime(), timePickerCalendar.getTime());
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(runnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UserData.saveData(getApplicationContext());
        handler.removeCallbacks(runnable);
    }
}
