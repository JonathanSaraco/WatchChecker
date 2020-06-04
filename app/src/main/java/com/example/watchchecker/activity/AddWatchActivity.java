package com.example.watchchecker.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import com.example.watchchecker.R;

public class AddWatchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_watch_popup_layout);

        // Change EditTexts to act as buttons to call a DatePicker
        EditText purchaseEditText = findViewById(R.id.purchaseDateEdit);
        setUpDatePickerEditText(purchaseEditText);
        EditText serviceEditText = findViewById(R.id.serviceDateEdit);
        setUpDatePickerEditText(serviceEditText);
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
}
