package com.example.watchchecker;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * An {@link TextWatcher} that causes the supplied {@link TextView} to be disabled if the text is
 * empty.
 */
public class RequiredFieldTextWatcher implements TextWatcher {

    private final TextView textView;

    public RequiredFieldTextWatcher(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        textView.setEnabled(!s.toString().trim().isEmpty());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
