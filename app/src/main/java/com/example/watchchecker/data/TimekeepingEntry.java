package com.example.watchchecker.data;

import com.example.watchchecker.util.Timekeeping_Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An {@link Iterable} used to store {@link } represent a timekeeping run. {}
 */
public class TimekeepingEntry implements Iterable<TimingEntry> {

    private final List<TimingEntry> timingEntries;

    public TimekeepingEntry() {
        this.timingEntries = new ArrayList<>();
    }

    /**
     * Calculates the time deviation for this entire timekeeping run
     */
    public TimingDeviation getTimingDeviation() {
        if (timingEntries.size() < 2) {
            return TimingDeviation.UNDEFINED_DEVIATION;
        } else {
            TimingEntry firstTimingEntry = timingEntries.get(0);
            TimingEntry lastTimingEntry = timingEntries.get(timingEntries.size() - 1);
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
        return timingEntries.iterator();
    }
}
