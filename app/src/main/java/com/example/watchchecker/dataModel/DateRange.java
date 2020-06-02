package com.example.watchchecker.dataModel;

import java.util.Date;

/**
 * Just a convenience class to store a starting date and an ending date
 */
public class DateRange {

    /**
     * The start of the data range
     */
    private final Date startDate;

    /**
     * The end of the date range
     */
    private final Date endDate;

    /**
     * A static instance that is used as a dummy value for initializations of {@link TimekeepingEntry}
     * objects.
     */
    public static final DateRange ZERO_DATE = new DateRange();

    public DateRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private DateRange() {
        this(new Date(), new Date());
    }

    /**
     * Get the start date
     * @return the start date, I just said
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Set the start date, probably not used but why not
     */
    public void setStartDate(Date startDate) {
        this.startDate.setTime(startDate.getTime());
    }

    /**
     * Get the end date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Set the end date
     */
    public void setEndDate(Date endDate) {
        this.endDate.setTime(endDate.getTime());
    }

}
