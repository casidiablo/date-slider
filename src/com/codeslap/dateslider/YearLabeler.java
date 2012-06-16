package com.codeslap.dateslider;

import java.util.Calendar;

/**
 * @author cristian
 * @version 1.0
 */
class YearLabeler extends DateSlider.Labeler {
    public YearLabeler(DateSlider dateSlider) {
        super(dateSlider);
    }

    @Override
    public DateSlider.TimeObject add(long time, int val) {
        // add "val" year to the month object that contains "time" and returns the new TimeObject
        Calendar c = Calendar.getInstance(getDateSlider().getTimeZone());
        c.setTimeInMillis(time);
        c.add(Calendar.YEAR, val);
        return timeObjectFromCalendar(c);
    }

    @Override
    protected DateSlider.TimeObject timeObjectFromCalendar(Calendar c) {
        // creates an TimeObject from a CalendarInstance
        int year = c.get(Calendar.YEAR);
        // set calendar to first millisecond of the year
        c.set(year, 0, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        long startTime = c.getTimeInMillis();
        // set calendar to last millisecond of the year
        c.set(year, 11, 31, 23, 59, 59);
        c.set(Calendar.MILLISECOND, 999);
        long endTime = c.getTimeInMillis();
        return new DateSlider.TimeObject(String.valueOf(year), startTime, endTime);
    }
}
