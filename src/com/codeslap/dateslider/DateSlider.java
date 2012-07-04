/*
 * Copyright (C) 2011 Daniel Berndt - Codeus Ltd  -  DateSlider
 * 
 * Class for setting up the dialog and initializing the underlying
 * ScrollLayouts
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

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public abstract class DateSlider extends Dialog {

    private final OnDateSetListener mOnDateSetListener;
    protected Calendar mTime;
    private TimeZone mTimeZone;
    protected TextView mTitleText;
    final List<ScrollLayout> mScrollerList = new ArrayList<ScrollLayout>();
    private LinearLayout mLayout;
    private CharSequence mMessageContent;
    private boolean mCancelButtonVisible = true;

    public DateSlider(Context context, OnDateSetListener l, Calendar calendar) {
        super(context);
        this.mOnDateSetListener = l;
        mTimeZone = calendar.getTimeZone();
        mTime = Calendar.getInstance(mTimeZone);
        mTime.setTimeInMillis(calendar.getTimeInMillis());
    }


    /**
     * Set up the dialog with all the views and their listeners
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            long time = savedInstanceState.getLong("time", mTime.getTimeInMillis());
            mTime.setTimeInMillis(time);
        }

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.date_slider);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.dialog_title);

        mTitleText = (TextView) findViewById(R.id.date_slider_title_text);
        mLayout = (LinearLayout) findViewById(R.id.dateSliderMainLayout);

        Button okButton = (Button) findViewById(R.id.date_slider_ok_button);
        okButton.setOnClickListener(mOkButtonClickListener);

        Button cancelButton = (Button) findViewById(R.id.date_slider_cancel_button);
        cancelButton.setOnClickListener(mCancelButtonClickListener);

        arrangeScroller(null);

        if (mMessageContent == null) {
            findViewById(R.id.message_container).setVisibility(View.GONE);
        } else {
            TextView message = (TextView) findViewById(R.id.message);
            message.setText(mMessageContent);
        }

        findViewById(R.id.date_slider_cancel_button).setVisibility(mCancelButtonVisible ? View.VISIBLE : View.GONE);
    }

    /**
     * This method allows to change the displayed time of the slider(s).
     * this can be handy if you need to invoke the dialog several times
     * using OnPrepareDialog.
     *
     * @param calendar the calendar object containing the new time
     */
    public void updateCalendar(Calendar calendar) {
        mTimeZone = calendar.getTimeZone();
        mTime = Calendar.getInstance(mTimeZone);
        mTime.setTimeInMillis(calendar.getTimeInMillis());
    }

    public TimeZone getTimeZone() {
        return mTimeZone;
    }

    private final android.view.View.OnClickListener mOkButtonClickListener = new android.view.View.OnClickListener() {
        public void onClick(View v) {
            if (mOnDateSetListener != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(getRoundedTime());
                mOnDateSetListener.onDateSet(DateSlider.this, calendar);
            }
            dismiss();
        }
    };

    private final android.view.View.OnClickListener mCancelButtonClickListener = new android.view.View.OnClickListener() {
        public void onClick(View v) {
            dismiss();
        }
    };

    /**
     * Sets the message of this dialog
     *
     * @param title the message
     */
    public void setMessage(CharSequence title) {
        mMessageContent = title;
    }

    /**
     * Sets the message of this dialog
     *
     * @param title the message
     */
    public void setMessage(int title) {
        mMessageContent = getContext().getString(title);
    }

    public void setCancelButtonVisible(boolean visible) {
        mCancelButtonVisible = visible;
    }

    /**
     * Sets the Scroll listeners for all ScrollLayouts in "mScrollerList"
     */
    void setListeners() {
        for (final ScrollLayout sl : mScrollerList) {
            sl.setOnScrollListener(
                    new ScrollLayout.OnScrollListener() {
                        public void onScroll(long x) {
                            mTime.setTimeInMillis(x);
                            arrangeScroller(sl);
                        }
                    });
        }

    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle savedInstanceState = super.onSaveInstanceState();
        if (savedInstanceState == null) savedInstanceState = new Bundle();
        savedInstanceState.putLong("time", mTime.getTimeInMillis());
        return savedInstanceState;
    }

    void arrangeScroller(ScrollLayout source) {
        setTitle();
        if (source != null) {
            for (ScrollLayout scroller : mScrollerList) {
                if (scroller == source) continue;
                scroller.setTime(mTime.getTimeInMillis(), 0);
            }
        }
    }

    /**
     * This method sets the title of the dialog
     */
    void setTitle() {
        if (mTitleText != null) {
            mTitleText.setText(getContext().getString(R.string.date_slider_title) +
                    String.format(": %te. %tB %tY", mTime, mTime, mTime));
        }
    }

    long getRoundedTime() {
        return mTime.getTimeInMillis();
    }

    /**
     * Defines the interface which defines the methods of the OnDateSetListener
     */
    public interface OnDateSetListener {
        /**
         * this method is called when a date was selected by the user
         *
         * @param view the caller of the method
         */
        public void onDateSet(DateSlider view, Calendar selectedDate);
    }

    /**
     * This class has the purpose of telling the corresponding scroller, which values make up
     * a single TimeTextView element.
     */
    public static abstract class Labeler {

        private final DateSlider mDateSlider;

        public Labeler(DateSlider dateSlider) {
            mDateSlider = dateSlider;
        }

        /**
         * gets called once, when the scroller gets initialised
         *
         * @param time the time in milliseconds
         * @return the TimeObject representing "time"
         */
        public TimeObject getElem(long time) {
            Calendar c = Calendar.getInstance(mDateSlider.mTimeZone);
            c.setTimeInMillis(time);
            return timeObjectFromCalendar(c);
        }

        /**
         * returns a new TimeTextView instance, is only called a couple of times in the
         * initialisation process
         *
         * @param context      used to create the view
         * @param isCenterView is true when the view is the central view
         * @return a TimeView instance
         */
        public TimeView createView(Context context, boolean isCenterView) {
            return new TimeView.TimeTextView(context, isCenterView, 25);
        }

        public DateSlider getDateSlider() {
            return mDateSlider;
        }

        /**
         * This method will be called constantly, whenever new date information is required
         * it receives a timestamps and adds "val" time units to that time and returns it as
         * a TimeObject
         *
         * @param time the time in milliseconds
         * @param val  days to add
         * @return new time object
         */
        public abstract TimeObject add(long time, int val);

        protected abstract TimeObject timeObjectFromCalendar(Calendar c);
    }

    protected void addSlider(ScrollLayout mMonthScroller, int index, LinearLayout.LayoutParams lp) {
        LinearLayout container = (LinearLayout) mLayout.findViewById(R.id.sliders_container);
        container.addView(mMonthScroller, index, lp);
    }

    /**
     * Very simple helper class that defines a time unit with a label (text) its start-
     * and end date
     */
    public static class TimeObject {
        public final CharSequence text;
        public final long startTime, endTime;

        public TimeObject(final CharSequence text, final long startTime, final long endTime) {
            this.text = text;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }
}
