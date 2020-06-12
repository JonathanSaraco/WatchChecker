package com.example.watchchecker.data;

import java.math.BigDecimal;

/**
 * Class to store a date range as two {@link DateString}s
 */
public class DateStringRange {

    private DateString startDateString;

    private DateString endDateString;

    private BigDecimal durationInDays = null;

    public static DateStringRange UNDEFINED_RANGE = new DateStringRange(new DateString());

    public DateStringRange(DateString startDateString) {
        this.startDateString = startDateString;
        this.endDateString = startDateString;
    }

    public DateStringRange(DateString startDateString, DateString endDateString) {
        this.startDateString = startDateString;
        this.endDateString = endDateString;
    }

    public BigDecimal getDurationInDays() {
        if (this.equals(UNDEFINED_RANGE)) {
            return new BigDecimal(1.);
        }
        if (durationInDays == null) {
            try {
                durationInDays = new BigDecimal ((endDateString.getComplexDate().getTime() - startDateString.getComplexDate().getTime()) / (1_000. * 60. * 60. * 24.));
            } catch (Exception e) {
                durationInDays = new BigDecimal(1.);
            }
        }
        return durationInDays;
    }

    public String toDisplayString() {
        if (this.equals(UNDEFINED_RANGE)) {
            return "UNDEFINED";
        }
        return String.format("%s %s", startDateString.getSimpleDateString(), endDateString.getSimpleDateString());
    }
}
