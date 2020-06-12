package com.example.watchchecker.activity;

import android.app.IntentService;
import android.content.Intent;

import com.example.watchchecker.data.UserData;

import java.io.IOException;

/**
 * {@link IntentService} to perform all finalization tasks for the app.
 */
public class WriteUserDataService extends IntentService {

    public WriteUserDataService() {
        super("finalizationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            UserData.writeUserData(WriteUserDataService.this);
            // Only set this to true if the writing was successful
            UserData.getWatchTimekeepingMap().setAllChangesCommitted();
        } catch (IOException ignore) {}
    }
}
