package com.example.watchchecker.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * A wrapper class to store a {@link Date} as a string, where it's accessed by parsing the string
 */

public class DateString {

    public static final DateFormat COMPLEX_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static final DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private final String complexDateString;

    private final String simpleDateString;

    public static DateString tryToParse(String complexDateString) {
        try {
            return new DateString(COMPLEX_DATE_FORMAT.parse(complexDateString));
        } catch (Exception e) {
            return new DateString();
        }
    }

    public DateString(Date date) {
        this.complexDateString = COMPLEX_DATE_FORMAT.format(date);
        this.simpleDateString = SIMPLE_DATE_FORMAT.format(date);
    }

    public DateString() {
        this.complexDateString = "";
        this.simpleDateString = "";
    }

    public Date getComplexDate() throws ParseException {
        return COMPLEX_DATE_FORMAT.parse(complexDateString);
    }

    public String getComplexDateString() {
        return complexDateString;
    }

    public Date getSimpleDate() throws ParseException {
        return SIMPLE_DATE_FORMAT.parse(simpleDateString);
    }

    public String getSimpleDateString() {
        return simpleDateString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateString that = (DateString) o;
        return Objects.equals(getComplexDateString(), that.getComplexDateString()) &&
                Objects.equals(getSimpleDateString(), that.getSimpleDateString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(complexDateString, getSimpleDateString());
    }
}
