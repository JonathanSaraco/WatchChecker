package com.example.watchchecker.util;

import com.example.watchchecker.dataModel.TimingDeviation;
import com.example.watchchecker.dataModel.TimingEntry;

import javax.measure.MetricPrefix;
import javax.measure.Quantity;
import javax.measure.quantity.Time;

import tec.units.ri.quantity.Quantities;
import tec.units.ri.unit.Units;

/**
 * Class for static utility methods for calculations involving {@link TimingEntry} objects.
 */
public class Timekeeping_Util {

    /**
     *
     * @param firstTimingEntry
     * @param lastTimingEntry
     * @return
     */
    public static TimingDeviation calculateDeviation(TimingEntry firstTimingEntry, TimingEntry lastTimingEntry) {
        // Get the deviation in seconds that has occurred over the days elapsed, is the numerator
        Number deviationOccurred = lastTimingEntry.getDelta().subtract(firstTimingEntry.getDelta()).to(Units.SECOND).getValue();
        // Return the result
        return new TimingDeviation(deviationOccurred.doubleValue() / durationInDays(lastTimingEntry, firstTimingEntry).doubleValue());
    }

    /**
     * @return the difference between two {@link Time}s, ie. {@param lastTime} minus {@param firstTime}
     * in days
     */
    private static Number durationInDays(TimingEntry lastTime, TimingEntry firstTime) {
        Quantity<Time> lastTimeQuantity = Quantities.getQuantity(lastTime.getReferenceTime().getTime(), MetricPrefix.MILLI(Units.SECOND));
        Quantity<Time> firstTimeQuantity = Quantities.getQuantity(firstTime.getReferenceTime().getTime(), MetricPrefix.MILLI(Units.SECOND));
        Quantity<Time> differenceTimeQuantity = lastTimeQuantity.subtract(firstTimeQuantity);
        return differenceTimeQuantity.to(Units.DAY).getValue();
    }
}
