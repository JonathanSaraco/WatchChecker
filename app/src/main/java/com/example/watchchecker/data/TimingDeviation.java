package com.example.watchchecker.data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Stores the calculated deviation from a reference time for a given {@link TimekeepingEntry} in units
 * of seconds per day.
 */
public class TimingDeviation {

    public static final TimingDeviation UNDEFINED_DEVIATION = new TimingDeviation(Double.MIN_VALUE);

    private final BigDecimal deviation;

    public TimingDeviation(BigDecimal deviation) {
        this.deviation = deviation;
    }

    public TimingDeviation(Double deviation) {
        this.deviation = new BigDecimal(deviation).setScale(4, RoundingMode.HALF_UP);
    }

    public BigDecimal toBigDecimal() {
        return deviation;
    }

    public Double toDoubleValue() {
        return this.deviation.doubleValue();
    }

    private boolean isUndefined() {
        return this.equals(UNDEFINED_DEVIATION) || this.toBigDecimal().doubleValue() == Double.MIN_VALUE;
    }

    public String toSimpleDisplayString()
    {
        if (isUndefined()) {
            return "UNDEFINED";
        } else {
            return String.format("%s %s", this.deviation.setScale(2, RoundingMode.HALF_UP).toPlainString(), "s");
        }
    }

    public String toFullDisplayString() {
        if (isUndefined()) {
            return "UNDEFINED";
        } else {
            return String.format("%s %s", this.deviation.setScale(2, RoundingMode.HALF_UP).toPlainString(), "s/day");
        }
    }
}
