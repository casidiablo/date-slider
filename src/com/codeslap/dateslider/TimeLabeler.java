package com.codeslap.dateslider;

import android.util.Log;

import java.util.Calendar;

/**
 * Time labeler takes care of providing each TimeTextView element in the time scroller
 * with the right label and information about its time representation
 *
 * @author cristian
 * @version 1.0
 */
class TimeLabeler extends DateSlider.Labeler {
    public TimeLabeler(DateSlider dateSlider) {
        super(dateSlider);
    }

    @Override
    public DateSlider.TimeObject add(long time, int val) {
        Calendar c = Calendar.getInstance(getDateSlider().getTimeZone());
        c.setTimeInMillis(time);
        c.add(Calendar.MINUTE, val * DateTimeSlider.MINUTE_INTERVAL);
        return timeObjectFromCalendar(c);
    }

    /**
     * override this method to set the initial TimeObject to a multiple of MINUTE_INTERVAL
     */
    @Override
    public DateSlider.TimeObject getElem(long time) {
        Calendar c = Calendar.getInstance(getDateSlider().getTimeZone());
        c.setTimeInMillis(time);
        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) / DateTimeSlider.MINUTE_INTERVAL * DateTimeSlider.MINUTE_INTERVAL);
        Log.v("GETELEM", "getelem: " + c.get(Calendar.MINUTE));
        return timeObjectFromCalendar(c);
    }

    /**
     * creates an TimeObject from a CalendarInstance
     */
    @Override
    protected DateSlider.TimeObject timeObjectFromCalendar(Calendar c) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE) / DateTimeSlider.MINUTE_INTERVAL * DateTimeSlider.MINUTE_INTERVAL;
        // get the last millisecond of that 15 minute block
        c.set(year, month, day, hour, minute + DateTimeSlider.MINUTE_INTERVAL - 1, 59);
        c.set(Calendar.MILLISECOND, 999);
        long endTime = c.getTimeInMillis();
        // get the first millisecond of that 15 minute block
        c.set(year, month, day, hour, minute, 0);
        c.set(Calendar.MILLISECOND, 0);
        long startTime = c.getTimeInMillis();
        return new DateSlider.TimeObject(String.format("%tR", c), startTime, endTime);
    }
}
