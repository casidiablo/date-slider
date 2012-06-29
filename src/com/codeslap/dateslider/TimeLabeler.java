package com.codeslap.dateslider;

import android.content.Context;
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
        Calendar calendar = Calendar.getInstance(getDateSlider().getTimeZone());
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.MINUTE, val * DateTimeSlider.MINUTE_INTERVAL);
        return timeObjectFromCalendar(calendar);
    }

    @Override
    public DateSlider.TimeObject getElem(long time) {
        Calendar calendar = Calendar.getInstance(getDateSlider().getTimeZone());
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) /
                DateTimeSlider.MINUTE_INTERVAL * DateTimeSlider.MINUTE_INTERVAL);
        Log.v("GETELEM", "getelem: " + calendar.get(Calendar.MINUTE));
        return timeObjectFromCalendar(calendar);
    }

    @Override
    protected DateSlider.TimeObject timeObjectFromCalendar(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE) / DateTimeSlider.MINUTE_INTERVAL * DateTimeSlider.MINUTE_INTERVAL;
        // get the last millisecond of that 15 minute block
        calendar.set(year, month, day, hour, minute + DateTimeSlider.MINUTE_INTERVAL - 1, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long endTime = calendar.getTimeInMillis();
        // get the first millisecond of that 15 minute block
        calendar.set(year, month, day, hour, minute, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();
        String label = String.format("%tI:%tM %tp", calendar, calendar, calendar);
        return new DateSlider.TimeObject(label, startTime, endTime);
    }

    @Override
    public TimeView createView(Context context, boolean isCenterView) {
        return new TimeView.TimeLayoutView(context, isCenterView, 25, 8, 0.95f);
    }
}
