package com.example.watchchecker.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Constraints the width of an {@link ImageView} based on its height to ensure it is square.
 */
public class SquareHeightImageView extends AppCompatImageView {

    public SquareHeightImageView(Context context) {
        super(context);
    }

    public SquareHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareHeightImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
    {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(height, height);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh)
    {
        super.onSizeChanged(h, h, oldw, oldh);
    }

}
