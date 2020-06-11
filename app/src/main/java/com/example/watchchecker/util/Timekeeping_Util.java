package com.example.watchchecker.util;

import android.content.SharedPreferences;

import com.example.watchchecker.data.TimingDeviation;
import com.example.watchchecker.data.TimingEntry;
import com.instacart.library.truetime.TrueTime;

import java.util.Date;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Time;

import tec.units.ri.quantity.Quantities;
import tec.units.ri.unit.Units;

/**
 * Class for static utility methods for calculations involving {@link TimingEntry} objects.
 */
public class Timekeeping_Util {

    // TODO do this
    private static SharedPreferences sharedPreferences = null;

    public static TimingDeviation calculateDeviation(TimingEntry firstTimingEntry, TimingEntry lastTimingEntry) {
        // Get the deviation in seconds that has occurred over the days elapsed, is the numerator
        Number deviationOccurred = lastTimingEntry.getDeviation().toDoubleValue() - firstTimingEntry.getDeviation().toDoubleValue();
        // Return the result
        return new TimingDeviation(deviationOccurred.doubleValue() / durationInDays(lastTimingEntry, firstTimingEntry).doubleValue());
    }

    public static TimingDeviation calculateDeviation(Date referenceDate, Date watchDate) {
        return new TimingDeviation((watchDate.getTime() - referenceDate.getTime()) / 1_000.);
    }

    /**
     * @return the difference between two {@link Time}s, ie. {@param lastTime} minus {@param firstTime}
     * in days
     */
    private static Number durationInDays(TimingEntry lastTime, TimingEntry firstTime) {
        Unit<Time> secondsUnit = Units.getInstance().getUnit(Time.class);
        Quantity<Time> lastTimeQuantity = Quantities.getQuantity(lastTime.getReferenceTime().getTime() / 1_000., secondsUnit);
        Quantity<Time> firstTimeQuantity = Quantities.getQuantity(firstTime.getReferenceTime().getTime() / 1_000., secondsUnit);
        Quantity<Time> differenceTimeQuantity = lastTimeQuantity.subtract(firstTimeQuantity);
        return differenceTimeQuantity.to(Units.DAY).getValue();
    }

    public static Date getReferenceTime() {
        // TODO implement the rest of this
        if (!TrueTime.isInitialized()) {
            try {
                TrueTime.build().initialize();
            } catch (Exception ignore) {}
        }
        return TrueTime.now();
    }

    public static Quantity<javax.measure.quantity.Time> getDateDeviation(Date referenceDate, Date watchDate) {
        return Quantities.getQuantity((watchDate.getTime() - referenceDate.getTime()) / 1_000., Units.getInstance().getUnit(Time.class));
    }
}
