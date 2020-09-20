package com.example.watchchecker.activity;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.watchchecker.data.UserData;
import com.example.watchchecker.data.WatchDataEntry;

import java.io.IOException;

/**
 * {@link IntentService} to perform all finalization tasks for the app.
 */
public class WriteUserDataService extends IntentService {

    private WatchDataEntry watchDataEntryToWrite;

    public WriteUserDataService() {
        super("finalizationService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null) {
            watchDataEntryToWrite = intent.getExtras().getParcelable(WatchDataEntry.PARCEL_KEY);
        } else {
            Log.e(WriteUserDataService.class.getSimpleName(), "No parcelable");
            throw new IllegalStateException();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            UserData.writeWatchDataEntry(WriteUserDataService.this, watchDataEntryToWrite);
            // Only set this to true if the writing was successful
            UserData.getWatchTimekeepingMap().setAllChangesCommitted();
        } catch (IOException ignore) {}
    }
}
