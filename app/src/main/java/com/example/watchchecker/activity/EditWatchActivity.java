package com.example.watchchecker.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;

import com.example.watchchecker.R;
import com.example.watchchecker.data.DateString;
import com.example.watchchecker.data.UserData;
import com.example.watchchecker.data.WatchDataEntry;
import com.example.watchchecker.util.ThemeUtil;

import java.text.ParseException;

public class EditWatchActivity extends Activity {

    WatchDataEntry watchDataEntryToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeUtil.getDialogThemeResourceID(ThemeUtil.getThemeFromPreferences(getApplicationContext())));
        setContentView(R.layout.add_watch_popup_layout);
        // Unparcel the WatchDataEntry that we are editing
        watchDataEntryToEdit = getIntent().getExtras().getParcelable(WatchDataEntry.PARCEL_KEY);
        // Get views and set their texts to correspond to
        EditText brandEditText = findViewById(R.id.brandEditText);
        setViewText(watchDataEntryToEdit.getBrand(), brandEditText);
        EditText modelEditText = findViewById(R.id.modelEditText);
        setViewText(watchDataEntryToEdit.getModel(), modelEditText);
        EditText movementEditText = findViewById(R.id.movementEditText);
        setViewText(watchDataEntryToEdit.getMovement(), movementEditText);

        // Change EditTexts to act as buttons to call a DatePicker
        EditText purchaseEditText = findViewById(R.id.purchaseDateEdit);
        setUpDatePickerEditText(watchDataEntryToEdit.getPurchaseDate(), purchaseEditText);
        EditText serviceEditText = findViewById(R.id.serviceDateEdit);
        setUpDatePickerEditText(watchDataEntryToEdit.getLastServiceDate(), serviceEditText);

        // Set behaviour of the add watch button
        Button editWatchButton = findViewById(R.id.addWatchButton);
        editWatchButton.setText("Edit");
        editWatchButton.setOnClickListener(v -> {
            WatchDataEntry entryWithNewInfo = null;
            try {
                entryWithNewInfo = new WatchDataEntry(brandEditText.getText().toString(),
                        modelEditText.getText().toString(),
                        movementEditText.getText().toString(),
                        parseDateEditText(purchaseEditText),
                        parseDateEditText(serviceEditText));
            } catch (ParseException ignore) {}
            if (entryWithNewInfo != null) {
                UserData.editWatchDataEntry(watchDataEntryToEdit, entryWithNewInfo);
                // We're done with the activity
                this.finish();
            }
        });
    }

    private void setViewText(String text, EditText editText) {
        if (!text.isEmpty()) {
            editText.setText(text);
        }
    }

    private void setUpDatePickerEditText(DateString initializedDateString, EditText purchaseEditText) {
        if (!initializedDateString.isEmpty()) {
            purchaseEditText.setText(initializedDateString.getSimpleDateString());
        }
        purchaseEditText.setInputType(InputType.TYPE_NULL);
        purchaseEditText.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int currentDay = cldr.get(Calendar.DAY_OF_MONTH);
            int currentMonth = cldr.get(Calendar.MONTH);
            int currentYear = cldr.get(Calendar.YEAR);
            // date picker dialog
            DatePickerDialog datePicker = new DatePickerDialog(EditWatchActivity.this,
                    (view, year, month, date) -> purchaseEditText.setText(String.format("%s/%s/%s", date, month, year)),
                    currentYear, currentMonth, currentDay);
            datePicker.show();
        });
    }

    private DateString parseDateEditText(EditText editText) throws ParseException {
        if (editText.getText().toString().isEmpty()) {
            return new DateString();
        } else {
            return new DateString(DateString.SIMPLE_DATE_FORMAT.parse(editText.getText().toString()));
        }
    }

    @Override
    protected void onStop() {
        UserData.saveData(getApplicationContext());
        super.onStop();
    }

}
