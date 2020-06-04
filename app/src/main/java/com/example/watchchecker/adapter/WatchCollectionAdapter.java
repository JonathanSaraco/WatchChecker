package com.example.watchchecker.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.watchchecker.R;
import com.example.watchchecker.dataModel.WatchDataEntry;
import com.example.watchchecker.view.SquareImageView;

import java.util.List;

public class WatchCollectionAdapter extends BaseAdapter {

    private final Context context;

    /**
     * The list of {@link WatchDataEntry} objects that form each element of the GridView
     */
    private final List<WatchDataEntry> watchDataEntries;

    public WatchCollectionAdapter(Context context, List<WatchDataEntry> watchDataEntries) {
        this.context = context;
        this.watchDataEntries = watchDataEntries;
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
        // Wrap text and watch image in a card view
        CardView cardView = new CardView(context);
        // Linear layout to display both sections
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        // Text view to display watch brand and model
        TextView watchDisplayTextView = new TextView(context);
        setCommonTextViewProperties(watchDisplayTextView);
        watchDisplayTextView.setTextColor(context.getResources().getColor(R.color.colorPrimaryText));
        watchDisplayTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.watch_collection_display_text_size));
        watchDisplayTextView.setText(watchDataEntry.toDisplayString());
        // TextView to display movement name
        TextView watchMovementTextView = new TextView(context);
        setCommonTextViewProperties(watchMovementTextView);
        watchMovementTextView.setTextColor(context.getResources().getColor(R.color.colorSecondaryText));
        watchMovementTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.watch_collection_movement_text_size));
        watchMovementTextView.setText(watchDataEntry.getMovement());
        // Image view to display image associated with watch
        ImageView watchDialImageView = new SquareImageView(context);
        watchDialImageView.setImageResource(R.drawable.watch_placeholder_image);
        watchDialImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        // Add views to cardview, add that to the linearLayout, and return it
        linearLayout.addView(watchDisplayTextView);
        linearLayout.addView(watchMovementTextView);
        linearLayout.addView(watchDialImageView);
        cardView.addView(linearLayout);
        return cardView;
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
