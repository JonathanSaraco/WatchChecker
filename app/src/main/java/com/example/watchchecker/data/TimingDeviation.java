package com.example.watchchecker.data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Stores the calculated deviation from a reference time for a given {@link TimekeepingEntry} in units
 * of seconds per day.
 */
public class TimingDeviation {

    public static final TimingDeviation UNDEFINED_DEVIATION = new TimingDeviation(-1.);

    private final BigDecimal deviation;

    public TimingDeviation(Double deviation) {
        this.deviation = new BigDecimal(deviation).setScale(4, RoundingMode.HALF_UP);
    }

    public BigDecimal toBigDecimal() {
        return deviation;
    }

    public Double toDoubleValue() {
        return this.deviation.doubleValue();
    }

    public String toSimpleDisplayString()
    {
        if (this.equals(UNDEFINED_DEVIATION)) {
            return "Undefined";
        } else {
            return this.deviation.toPlainString();
        }
    }

    public String toFullDisplayString() {
        if (this.equals(UNDEFINED_DEVIATION)) {
            return "Undefined";
        } else {
            return String.format("%s %s", this.deviation.toPlainString(), "s/day");
        }
    }
}
