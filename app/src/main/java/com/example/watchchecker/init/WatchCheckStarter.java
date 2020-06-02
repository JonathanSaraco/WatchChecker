package com.example.watchchecker.init;

import android.os.AsyncTask;

/**
 * {@link Runnable} used to perform all initialization steps required for the app.
 */
public class WatchCheckStarter implements Runnable {

    @Override
    public void run() {
        AsyncTask.execute(new TimeInitialization());
    }
}
