package com.example.watchchecker.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;

import com.example.watchchecker.R;
import com.example.watchchecker.RequiredFieldTextWatcher;
import com.example.watchchecker.data.UserData;
import com.example.watchchecker.data.WatchDataEntry;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddWatchActivity extends Activity{

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_watch_popup_layout);

        // Get views for access following an add watch button click
        EditText brandEditText = findViewById(R.id.brandEditText);
        EditText modelEditText = findViewById(R.id.modelEditText);
        EditText movementEditText = findViewById(R.id.movementEditText);

        // Change EditTexts to act as buttons to call a DatePicker
        EditText purchaseEditText = findViewById(R.id.purchaseDateEdit);
        setUpDatePickerEditText(purchaseEditText);
        EditText serviceEditText = findViewById(R.id.serviceDateEdit);
        setUpDatePickerEditText(serviceEditText);

        // Set behaviour of the add watch button
        Button addWatchButton = findViewById(R.id.addWatchButton);
        addWatchButton.setOnClickListener(v -> {
            WatchDataEntry watchDataEntry = null;
            // Parse items in list
            try {
                watchDataEntry = new WatchDataEntry(brandEditText.getText().toString(),
                        modelEditText.getText().toString(),
                        movementEditText.getText().toString(),
                        parseDateEditText(purchaseEditText),
                        parseDateEditText(serviceEditText));
            } catch (ParseException ignore) {}
            if (watchDataEntry != null) {
                UserData.addWatchDataEntry(watchDataEntry);
                // We're done with the activity
                this.finish();
            }
        });
        // Disable add watch button if the model field is empty
        modelEditText.addTextChangedListener(new RequiredFieldTextWatcher(addWatchButton));
    }

    private void setUpDatePickerEditText(EditText purchaseEditText) {
        purchaseEditText.setInputType(InputType.TYPE_NULL);
        purchaseEditText.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int currentDay = cldr.get(Calendar.DAY_OF_MONTH);
            int currentMonth = cldr.get(Calendar.MONTH);
            int currentYear = cldr.get(Calendar.YEAR);
            // date picker dialog
            DatePickerDialog datePicker = new DatePickerDialog(AddWatchActivity.this,
                    (view, year, month, date) -> purchaseEditText.setText(String.format("%s/%s/%s", date, month, year)),
                    currentYear, currentMonth, currentDay);
            datePicker.show();
        });
    }

    private Date parseDateEditText(EditText editText) throws ParseException {
        if (editText.getText().toString().isEmpty()) {
            return new Date();
        } else {
            return DATE_FORMAT.parse(editText.getText().toString());
        }
    }
}
