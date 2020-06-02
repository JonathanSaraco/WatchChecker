package com.example.watchchecker.init;

import com.instacart.library.truetime.TrueTime;

/**
 * Instantiates and initializes static time-related classes, such as {@link TrueTime}.
 */
public class TimeInitialization implements Runnable {

    @Override
    public void run() {
        try {
            TrueTime.build().initialize();
        } catch (Exception ignore) {}
    }
}
