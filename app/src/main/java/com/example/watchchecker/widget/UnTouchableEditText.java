package com.example.watchchecker.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * An {@link EditText} that doesn't allow you to get a {@link this#onTouchEvent(MotionEvent)}, as
 * it will immediately prompt {@link this#performClick()} and suppress the touch.
 */
@SuppressLint("AppCompatCustomView")
public class UnTouchableEditText extends EditText {

    public UnTouchableEditText(Context context) {
        super(context);
    }

    public UnTouchableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnTouchableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        return false;
    }
}
