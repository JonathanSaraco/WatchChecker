package com.example.watchchecker.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;

import com.example.watchchecker.R;
import com.example.watchchecker.activity.WatchInformationDisplayActivity;
import com.example.watchchecker.data.TimekeepingEntry;
import com.example.watchchecker.data.UserData;
import com.example.watchchecker.data.WatchDataEntry;
import com.example.watchchecker.util.IO_Util;
import com.example.watchchecker.util.ImageUtil;
import com.example.watchchecker.util.IntentUtil;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class WatchCollectionAdapter extends BaseAdapter implements Observer {

    private final Context context;

    /**
     * The list of {@link WatchDataEntry} objects that form each element of the GridView
     */
    private List<WatchDataEntry> watchDataEntries;

    public WatchCollectionAdapter(Context context, List<WatchDataEntry> watchDataEntries) {
        this.context = context;
        this.watchDataEntries = watchDataEntries;
        UserData.getWatchTimekeepingMap().addObserver(this);
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.watch_collection_grid_view_element, null);
            // Text view to display watch brand and model
            TextView watchDisplayTextView = convertView.findViewById(R.id.display_watch_element_name_text_view);
            // TextView to display movement name
            TextView watchMovementTextView = convertView.findViewById(R.id.display_watch_element_movement_text_view);
            // Image view to display image associated with watch
            ImageView watchDialImageView = convertView.findViewById(R.id.display_watch_element_image_view);
            // Set on-click event of settings button
            ImageButton settingsButton = convertView.findViewById(R.id.watch_collection_element_settings_button);
            // With all views found, create a ViewHolder
            viewHolder = new ViewHolder(watchDataEntries.get(position),
                    watchDisplayTextView,
                    watchMovementTextView,
                    watchDialImageView,
                    settingsButton);
            // Put viewholder on convertview
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Set view values conveniently using ViewHolder
        if (getItem(position) != null) {
            WatchDataEntry watchDataEntry = watchDataEntries.get(position);
            viewHolder.watchDisplayTextView.setText(watchDataEntry.toDisplayString());
            viewHolder.watchMovementTextView.setText(watchDataEntry.getMovement());
            viewHolder.watchDialImageView.setImageBitmap(ImageUtil.getWatchDataEntryImage(context, watchDataEntry));
            // Setup on-click listener to display this watch entry's information
            convertView.setOnClickListener(v -> {
                // Setup and start activity to display timekeeping information
                Intent intent = new Intent(context, WatchInformationDisplayActivity.class);
                Bundle bundle = new Bundle();
                // Must use position to get watchDataEntry, otherwise there will be a mismatch
                bundle.putParcelable(WatchDataEntry.PARCEL_KEY, watchDataEntry);
                intent.putExtras(bundle);
                context.startActivity(intent);
            });
            // Final convertView used for lambdas, etc.
            View finalConvertView = convertView;
            viewHolder.settingsButton.setOnClickListener(v -> {
                // Instantiate and inflate popup menu
                PopupMenu popupMenu = new PopupMenu(context, viewHolder.settingsButton);
                popupMenu.getMenuInflater().inflate(R.menu.watch_collection_element_settings_menu, popupMenu.getMenu());
                // Set menu item onclick listener behaviour
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.watch_collection_element_settings_edit:
                            // User chose edit
                            break;
                        case R.id.watch_collection_element_settings_delete:
                            // User chose delete
                            // Store data entry temporarily in case the user presses undo
                            List<TimekeepingEntry> backupEntries = new ArrayList<>(UserData.getTimekeepingEntries(watchDataEntry));
                            // Remove data entry and setup snackbar with undo button
                            UserData.removeWatchDataEntry(watchDataEntry);
                            Snackbar.make(finalConvertView, String.format("Deleted: %s", watchDataEntry.toDisplayString()), Snackbar.LENGTH_LONG)
                                    .setAction("Undo", v1 -> UserData.addWatchDataEntry(watchDataEntry, backupEntries))
                                    .show();
                            break;
                        case R.id.watch_collection_element_settings_photo:
                            // User chose to take a photo, give them a dialog to choose their source
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Select an image source");
                            builder.setItems(ImageUtil.imageChoices, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // We can only parcel if the image is just a thumbnail, but we want more than that
                                    // so unfortunately we have to resort to this static field
                                    IO_Util.setWatchDataEntryForNewImage(watchDataEntry);
                                    // Launch different activities based on the user's choice
                                    if (ImageUtil.imageChoices[which].equals(ImageUtil.TAKE_PHOTO_CHOICE)) {
                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                                            File watchImageFile = null;
                                            try {
                                                watchImageFile = IO_Util.getImageFile(context);
                                            } catch (IOException e) {
                                                Log.i("WatchCollectionAdapter", "Failed to create image file in storage.");
                                            }
                                            if (watchImageFile != null) {
                                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, context.getPackageName(), watchImageFile));
                                                // Again, need to use a static field to store things
                                                IO_Util.setImagePathForNewImage(watchImageFile.getPath());
                                                ((Activity) context).startActivityForResult(takePictureIntent, IntentUtil.CAMERA_INTENT_REQUEST_CODE);
                                            }
                                        }
                                    } else if (ImageUtil.imageChoices[which].equals(ImageUtil.ATTACH_PHOTO_CHOICE)) {
                                        Intent intent = new Intent(Intent.ACTION_PICK);
                                        intent.setType("image/*");
                                        String[] mimeTypes = {"image/jpeg", "image/png"};
                                        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                                        // Start intent
                                        ((Activity) context).startActivityForResult(intent, IntentUtil.GALLERY_INTENT_REQUEST_CODE);
                                    }
                                }
                            });
                            builder.show();
                        default:
                            // Something went horribly wrong
                            return false;
                    }
                    return true;
                });
                popupMenu.show();
            });
        }
        return convertView;
    }


    @Override
    public void update(Observable o, Object arg) {
        watchDataEntries = UserData.getWatchDataEntries();
        notifyDataSetChanged();
    }

    /**
     * Class to store {@link View}s that are obtained by {@link View#findViewById(int)} so
     */
    static class ViewHolder {

        WatchDataEntry watchDataEntry;
        TextView watchDisplayTextView;
        TextView watchMovementTextView;
        ImageView watchDialImageView;
        ImageButton settingsButton;

        public ViewHolder(WatchDataEntry watchDataEntry,
                          TextView watchDisplayTextView,
                          TextView watchMovementTextView,
                          ImageView watchDialImageView,
                          ImageButton settingsButton) {
            this.watchDataEntry = watchDataEntry;
            this.watchDisplayTextView = watchDisplayTextView;
            this.watchMovementTextView = watchMovementTextView;
            this.watchDialImageView = watchDialImageView;
            this.settingsButton = settingsButton;
        }
    }

}
