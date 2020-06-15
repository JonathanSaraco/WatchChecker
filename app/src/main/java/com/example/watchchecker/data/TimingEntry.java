package com.example.watchchecker.data;


import com.example.watchchecker.util.Timekeeping_Util;

import java.text.ParseException;
import java.util.Date;

/**
 * A class to store each timing event, fields are the time of the watch, the NIST time it was compared
 * to, and the delta between them
 */
public class TimingEntry {

    /**
     * The time that the user has specified their watch will be displaying when they lock in a
     * comparison with NIST time
     */
    private final DateString watchTime;

    /**
     * The reference time when the user presses the check watch button
     */
    private final DateString referenceTime;

    /**
     * Long between the watch's time and the reference time in seconds
     */
    private final TimingDeviation deviation;

    public TimingEntry(Date watchTime, Date referenceTime) {
        this.watchTime = new DateString(watchTime);
        this.referenceTime = new DateString(referenceTime);
        this.deviation = Timekeeping_Util.calculateDeviation(referenceTime, watchTime);
    }

    public DateString getWatchDateString() {
        return watchTime;
    }

    public Date getWatchTime() {
        try {
            return watchTime.getComplexDate();
        } catch (ParseException e) {
            throw new IllegalStateException();
        }
    }

    public DateString getReferenceDateString() {
        return referenceTime;
    }

    public Date getReferenceTime() {
        try {
            return referenceTime.getComplexDate();
        } catch (ParseException e) {
            throw new IllegalStateException();
        }
    }

    public TimingDeviation getDeviation() {
        return deviation;
    }
}
