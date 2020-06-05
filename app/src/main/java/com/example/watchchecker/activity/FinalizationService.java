package com.example.watchchecker.activity;

import android.app.IntentService;
import android.content.Intent;

import com.example.watchchecker.data.UserData;

import java.io.IOException;

/**
 * {@link IntentService} to perform all finalization tasks for the app.
 */
public class FinalizationService extends IntentService {

    public FinalizationService() {
        super("finalizationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            UserData.writeUserData(FinalizationService.this);
        } catch (IOException ignore) {}
    }
}
