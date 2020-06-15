package com.example.watchchecker.data;

import com.example.watchchecker.util.Timekeeping_Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * An {@link Iterable} used to store {@link } represent a timekeeping run. {}
 */
public class TimekeepingEntry implements Iterable<TimingEntry> {

    private final List<TimingEntry> timingEntries;

    private TimingDeviation deviation = null;

    private DateStringRange dateStringRange = null;

    public TimekeepingEntry() {
        this.timingEntries = new ArrayList<>();
    }

    /**
     * Calculates the time deviation for this entire timekeeping run
     */
    public TimingDeviation getTimingDeviation() {
        if (deviation == null) {
            if (timingEntries.size() < 2) {
                deviation = TimingDeviation.UNDEFINED_DEVIATION;
            } else {
                TimingEntry firstTimingEntry = timingEntries.get(0);
                TimingEntry lastTimingEntry = timingEntries.get(timingEntries.size() - 1);
                deviation = Timekeeping_Util.calculateDeviation(firstTimingEntry, lastTimingEntry);
            }
        }
        return deviation;
    }

    public DateStringRange getDateStringRange() {
        if (dateStringRange == null) {
            if (timingEntries.isEmpty()) {
                dateStringRange = DateStringRange.UNDEFINED_RANGE;
            } else {
                dateStringRange = new DateStringRange(timingEntries.get(0).getReferenceDateString(),
                        timingEntries.get(timingEntries.size() - 1).getReferenceDateString());
            }
        }
        return dateStringRange;
    }

    public void addTimingEntry(TimingEntry timingEntry) {
        resetCalculatedFields();
        timingEntries.add(timingEntry);
    }

    public void removeTimingEntry(TimingEntry timingEntry) {
        resetCalculatedFields();
        timingEntries.remove(timingEntry);
    }

    public List<TimingEntry> getTimingEntries() {
        return timingEntries;
    }

    public Date getLastTimekeepingEvent() {
        if (!timingEntries.isEmpty()) {
            Date lastReferenceTime = timingEntries.get(timingEntries.size() - 1).getReferenceTime();
            if (lastReferenceTime != null) {
                return lastReferenceTime;
            }
        }
        return Timekeeping_Util.getReferenceTime();
    }

    public String getDateRangeDisplayString() {
        if (timingEntries.isEmpty()) {
            return "N/A";
        } else {
            TimingEntry firstTimingEntry = timingEntries.get(0);
            TimingEntry secondTimingEntry = timingEntries.get(timingEntries.size() - 1);
            return new DateStringRange(firstTimingEntry.getReferenceDateString(), secondTimingEntry.getReferenceDateString())
                    .toDisplayString();
        }
    }

    private void resetCalculatedFields() {
        deviation = null;
        dateStringRange = null;
    }

    @Override
    public Iterator<TimingEntry> iterator() {
        return timingEntries.iterator();
    }
}
