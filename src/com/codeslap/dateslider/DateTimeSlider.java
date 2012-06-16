/*
 * Copyright (C) 2011 Daniel Berndt - Codeus Ltd  -  DateSlider
 * 
 * DateSlider which allows for selecting of a date including a time 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codeslap.dateslider;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout.LayoutParams;

import java.util.Calendar;

public class DateTimeSlider extends DateSlider {

    public static final int MINUTE_INTERVAL = 15;

    public DateTimeSlider(Context context, OnDateSetListener l, Calendar calendar) {
        super(context, l, calendar);
    }

    /**
     * Create the month, day and the time scroller and feed them with their labelers
     * and place them on the layout.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // this needs to be called before everything else to set up the main layout of the DateSlider
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

        // create the month scroller and assign its labeler and add it to the layout
        ScrollLayout mMonthScroller = (ScrollLayout) inflater.inflate(R.layout.scroller, null);
        mMonthScroller.setLabeler(new MonthLabeler(this, true), mTime.getTimeInMillis(), 180, 60);
        mLayout.addView(mMonthScroller, 0, lp);
        mScrollerList.add(mMonthScroller);

        // create the month scroller and assign its labeler and add it to the layout
        ScrollLayout mDayScroller = (ScrollLayout) inflater.inflate(R.layout.scroller, null);
        mDayScroller.setLabeler(new DayLabeler(this), mTime.getTimeInMillis(), 120, 60);
        mLayout.addView(mDayScroller, 1, lp);
        mScrollerList.add(mDayScroller);

        // create the minute scroller and assign its labeler and add it to the layout
        ScrollLayout mTimeScroller = (ScrollLayout) inflater.inflate(R.layout.scroller, null);
        mTimeScroller.setLabeler(new TimeLabeler(this), mTime.getTimeInMillis(), 80, 60);
        mLayout.addView(mTimeScroller, 2, lp);
        mScrollerList.add(mTimeScroller);

        // this method _has_ to be called to set the onScrollListeners for all the Scrollers
        // in the mScrollerList.
        setListeners();
    }

    @Override
    protected void setTitle() {
        if (mTitleText != null) {
            int minute = mTime.get(Calendar.MINUTE) / MINUTE_INTERVAL * MINUTE_INTERVAL;
            mTitleText.setText(String.format("Selected DateTime: %te/%tm/%ty %tH:%02d",
                    mTime, mTime, mTime, mTime, minute));
        }
    }
}