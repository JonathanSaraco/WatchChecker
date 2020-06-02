package com.example.watchchecker.dataModel;

import com.example.watchchecker.util.Timekeeping_Util;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An {@link Iterable} used to store {@link } represent a timekeeping run. {}
 */
public class TimekeepingEntry implements Iterable<TimingEntry> {

    private final List<TimingEntry> timingEntries;

    private DateRange dateRange;

    public TimekeepingEntry() {
        this.timingEntries = new ArrayList<>();
        this.dateRange = DateRange.ZERO_DATE;
    }

    /**
     * Calculates the time deviation for this entire timekeeping run
     */
    public TimingDeviation getSecondsPerDay() {
        if (timingEntries.size() < 2) {
            return TimingDeviation.UNDEFINED_DEVIATION;
        } else {
            TimingEntry firstTimingEntry = Iterables.getFirst(timingEntries, null);
            TimingEntry lastTimingEntry = Iterables.getLast(timingEntries);
            return Timekeeping_Util.calculateDeviation(firstTimingEntry, lastTimingEntry);
        }
    }

    public void addTimingEntry(TimingEntry timingEntry) {
        timingEntries.add(timingEntry);
    }

    public void removeTimingEntry(TimingEntry timingEntry) {
        timingEntries.remove(timingEntry);
    }

    @Override
    public Iterator<TimingEntry> iterator() {
        return null;
    }
}
