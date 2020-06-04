package com.example.watchchecker.data;


import java.sql.Time;

import javax.measure.MetricPrefix;
import javax.measure.Quantity;

import tec.units.ri.quantity.Quantities;
import tec.units.ri.unit.Units;

/**
 * A class to store each timing event, fields are the time of the watch, the NIST time it was compared
 * to, and the delta between them
 */
public class TimingEntry {

    /**
     * The time that the user has specified their watch will be displaying when they lock in a
     * comparison with NIST time
     */
    private final Time watchTime;

    /**
     * The NIST time when the user presses the check watch button
     */
    private final Time referenceTime;

    /**
     * Long between the watch's time and the reference time in seconds
     */
    private final Quantity<javax.measure.quantity.Time> delta;

    public TimingEntry(Time watchTime, Time referenceTime) {
        this.watchTime = watchTime;
        this.referenceTime = referenceTime;
        this.delta = Quantities.getQuantity(referenceTime.getTime() - watchTime.getTime(), MetricPrefix.MILLI(Units.SECOND));
    }

    public Time getWatchTime() {
        return watchTime;
    }

    public Time getReferenceTime() {
        return referenceTime;
    }

    public Quantity<javax.measure.quantity.Time> getDelta() {
        return delta;
    }
}
