package com.example.watchchecker.util;

import android.content.SharedPreferences;

import com.example.watchchecker.data.TimekeepingEntry;
import com.example.watchchecker.data.TimingDeviation;
import com.example.watchchecker.data.TimingEntry;
import com.example.watchchecker.data.UserData;
import com.example.watchchecker.data.WatchDataEntry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public static TimingDeviation calculateAverageDeviation(WatchDataEntry watchDataEntry) {
        List<TimekeepingEntry> timekeepingEntries = UserData.getTimekeepingEntries(watchDataEntry);
        // Take care of trivial cases
        if (timekeepingEntries.isEmpty()) {
            return TimingDeviation.UNDEFINED_DEVIATION;
        } else if (timekeepingEntries.size() == 1) {
            return timekeepingEntries.get(0).getTimingDeviation();
        } // Return weighted average of timing deviations (deviations that occurred over a long period
          // are considered more accurate
        else {
            return calculateAverageDeviation(timekeepingEntries);
        }
    }

    /**
     * Calculate average deviation as the weighted average of the TimingDeviations, weighed by
     * the number of days elapsed over the timekeeping run
     */

    public static TimingDeviation calculateAverageDeviation(List<TimekeepingEntry> timekeepingEntries) {

        BigDecimal weightedSum = new BigDecimal(0.);
        BigDecimal sumOfWeights = new BigDecimal(0.);
        for (int i=0; i < timekeepingEntries.size(); i++) {
            TimekeepingEntry timekeepingEntry = timekeepingEntries.get(i);
            BigDecimal elapsedTimeInDays = timekeepingEntry.getDateStringRange().getDurationInDays();
            weightedSum = weightedSum.add(timekeepingEntry.getTimingDeviation().toBigDecimal().multiply(elapsedTimeInDays));
            sumOfWeights = sumOfWeights.add(elapsedTimeInDays);
        }
        return new TimingDeviation(weightedSum.divide(sumOfWeights, RoundingMode.HALF_UP));
    }

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
        return Calendar.getInstance().getTime();
    }

    public static Quantity<javax.measure.quantity.Time> getDateDeviation(Date referenceDate, Date watchDate) {
        return Quantities.getQuantity((watchDate.getTime() - referenceDate.getTime()) / 1_000., Units.getInstance().getUnit(Time.class));
    }
}
