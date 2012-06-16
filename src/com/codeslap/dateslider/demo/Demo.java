/*
 * Copyright (C) 2011 Daniel Berndt - Codeus Ltd  -  DateSlider
 * 
 * This is a small demo application which demonstrates the use of the
 * dateSelector
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

package com.codeslap.dateslider.demo;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.codeslap.dateslider.*;

import java.util.Calendar;

/**
 * Small Demo activity which demonstrates the use of the DateSlideSelector
 *
 * @author Daniel Berndt - Codeus Ltd
 */
public class Demo extends Activity {
    private static final int MINUTE_INTERVAL = 15;

    private static final int DEFAULT_DATE_SELECTOR_ID = 0;
    private static final int ALTERNATIVE_DATE_SELECTOR_ID = 1;
    private static final int CUSTOM_DATE_SELECTOR_ID = 2;
    private static final int MONTH_YEAR_DATE_SELECTOR_ID = 3;
    private static final int TIME_SELECTOR_ID = 4;
    private static final int DATE_TIME_SELECTOR_ID = 5;

    private TextView mDateText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // load and initialise the Activity
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        mDateText = new TextView(this);

        Button defaultButton = new Button(this);
        defaultButton.setText("Select DefaultDate");
        linearLayout.addView(defaultButton);
        // set up a listener for when the button is pressed 
        defaultButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // call the internal showDialog method using the predefined ID
                showDialog(DEFAULT_DATE_SELECTOR_ID);
            }
        });

        Button alternativeButton = new Button(this);
        alternativeButton.setText("Select AlternativeDate");
        linearLayout.addView(alternativeButton);
        // set up a listener for when the button is pressed 
        alternativeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // call the internal showDialog method using the predefined ID
                showDialog(ALTERNATIVE_DATE_SELECTOR_ID);
            }
        });

        Button customButton = new Button(this);
        customButton.setText("Select CustomDate");
        linearLayout.addView(customButton);
        // set up a listener for when the button is pressed 
        customButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // call the internal showDialog method using the predefined ID
                showDialog(CUSTOM_DATE_SELECTOR_ID);
            }
        });

        Button monthYearButton = new Button(this);
        monthYearButton.setText("Select Month/Year");
        linearLayout.addView(monthYearButton);
        // set up a listener for when the button is pressed 
        monthYearButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // call the internal showDialog method using the predefined ID
                showDialog(MONTH_YEAR_DATE_SELECTOR_ID);
            }
        });

        Button timeButton = new Button(this);
        timeButton.setText("Select Time");
        linearLayout.addView(timeButton);
        // set up a listener for when the button is pressed
        timeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // call the internal showDialog method using the predefined ID
                showDialog(TIME_SELECTOR_ID);
            }
        });

        Button dateTimeButton = new Button(this);
        dateTimeButton.setText("Select DateTime");
        linearLayout.addView(dateTimeButton);
        // set up a listener for when the button is pressed 
        dateTimeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // call the internal showDialog method using the predefined ID
                showDialog(DATE_TIME_SELECTOR_ID);
            }
        });

        linearLayout.addView(mDateText);
        setContentView(linearLayout);
    }

    // define the listener which is called once a user selected the date.
    private DateSlider.OnDateSetListener mDateSetListener =
            new DateSlider.OnDateSetListener() {
                public void onDateSet(DateSlider view, Calendar selectedDate) {
                    // update the mDateText view with the corresponding date
                    mDateText.setText(String.format("The chosen date:%n%te. %tB %tY", selectedDate, selectedDate, selectedDate));
                }
            };

    private DateSlider.OnDateSetListener mMonthYearSetListener =
            new DateSlider.OnDateSetListener() {
                public void onDateSet(DateSlider view, Calendar selectedDate) {
                    // update the mDateText view with the corresponding date
                    mDateText.setText(String.format("The chosen date:%n%tB %tY", selectedDate, selectedDate));
                }
            };

    private DateSlider.OnDateSetListener mTimeSetListener =
            new DateSlider.OnDateSetListener() {
                public void onDateSet(DateSlider view, Calendar selectedDate) {
                    // update the mDateText view with the corresponding date
                    mDateText.setText(String.format("The chosen time:%n%tR", selectedDate));
                }
            };

    private DateSlider.OnDateSetListener mDateTimeSetListener =
            new DateSlider.OnDateSetListener() {
                public void onDateSet(DateSlider view, Calendar selectedDate) {
                    // update the mDateText view with the corresponding date
                    int minute = selectedDate.get(Calendar.MINUTE) /
                            MINUTE_INTERVAL * MINUTE_INTERVAL;
                    mDateText.setText(String.format("The chosen date and time:%n%te. %tB %tY%n%tH:%02d",
                            selectedDate, selectedDate, selectedDate, selectedDate, minute));
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        // this method is called after invoking 'showDialog' for the first time
        // here we initiate the corresponding DateSlideSelector and return the dialog to its caller
        // get today's date and the time
        final Calendar c = Calendar.getInstance();
        switch (id) {
            case DEFAULT_DATE_SELECTOR_ID:
                return new DefaultDateSlider(this, mDateSetListener, c);
            case ALTERNATIVE_DATE_SELECTOR_ID:
                return new AlternativeDateSlider(this, mDateSetListener, c);
            case CUSTOM_DATE_SELECTOR_ID:
                return new CustomDateSlider(this, mDateSetListener, c);
            case MONTH_YEAR_DATE_SELECTOR_ID:
                return new MonthYearDateSlider(this, mMonthYearSetListener, c);
            case TIME_SELECTOR_ID:
                return new TimeSlider(this, mTimeSetListener, c);
            case DATE_TIME_SELECTOR_ID:
                return new DateTimeSlider(this, mDateTimeSetListener, c);
        }
        return null;
    }
}