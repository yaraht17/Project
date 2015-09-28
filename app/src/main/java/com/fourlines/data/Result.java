/**
 * Copyright (C) 2014 Samsung Electronics Co., Ltd. All rights reserved.
 * <p/>
 * Mobile Communication Division,
 * Digital Media & Communications Business, Samsung Electronics Co., Ltd.
 * <p/>
 * This software and its documentation are confidential and proprietary
 * information of Samsung Electronics Co., Ltd.  No part of the software and
 * documents may be copied, reproduced, transmitted, translated, or reduced to
 * any electronic medium or machine-readable form without the prior written
 * consent of Samsung Electronics.
 * <p/>
 * Samsung Electronics makes no representations with respect to the contents,
 * and assumes no responsibility for any errors that might appear in the
 * software and documents. This publication and the contents hereof are subject
 * to change without notice.
 */

package com.fourlines.data;

import android.database.Cursor;
import android.util.Log;

import com.fourlines.fragment.ProfileFragment;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataObserver;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataResolver.Filter;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadRequest;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadResult;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import java.util.Calendar;

public class Result {
    private final HealthDataStore mStore;
    private ProfileFragment profileFragment;

    public Result(HealthDataStore store) {
        mStore = store;
    }

    public void start() {
        // Register an observer to listen changes of step count and get today step count
        HealthDataObserver.addObserver(mStore, HealthConstants.StepCount.HEALTH_DATA_TYPE, mObserver);
        HealthDataObserver.addObserver(mStore, HealthConstants.HeartRate.HEALTH_DATA_TYPE, mObserver);
        HealthDataObserver.addObserver(mStore, HealthConstants.Weight.HEALTH_DATA_TYPE, mObserver);
        readWeightAndHeight();
        readHeartRate();
        readStepCount();


    }

    // Read the today's step count on demand
    private void readStepCount() {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        // Set time range from start time of today to the current time
        long startTime = getStartTimeOfToday();
        Filter filter = Filter.and(Filter.eq(HealthConstants.StepCount.START_TIME, startTime));

        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                .setDataType(HealthConstants.StepCount.HEALTH_DATA_TYPE)
                .setProperties(new String[]{HealthConstants.StepCount.COUNT})
                .setFilter(filter)
                .build();
        try {
            resolver.read(request).setResultListener(mListenerStepCount);
        } catch (Exception e) {
            Log.e("LinhTh", e.getClass().getName() + " - " + e.getMessage());
        }
    }

    private void readHeartRate() {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                .setDataType(HealthConstants.HeartRate.HEALTH_DATA_TYPE)
                .setProperties(new String[]{HealthConstants.HeartRate.HEART_RATE})
                .setFilter(null)
                .build();

        try {
            resolver.read(request).setResultListener(mListenerHeartRate);
        } catch (Exception e) {
            Log.e("LinhTh", e.getClass().getName() + " - " + e.getMessage());
        }
    }

    private void readWeightAndHeight() {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                .setDataType(HealthConstants.Weight.HEALTH_DATA_TYPE)
                .setProperties(new String[]{HealthConstants.Weight.HEIGHT, HealthConstants.Weight.WEIGHT})
                .setFilter(null)
                .build();

        try {
            resolver.read(request).setResultListener(mListenerWeightHeight);
        } catch (Exception e) {
            Log.e("LinhTh", e.getClass().getName() + " - " + e.getMessage());
        }
    }

    private long getStartTimeOfToday() {
        Calendar today = Calendar.getInstance();

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return today.getTimeInMillis();
    }

    private final HealthResultHolder.ResultListener<ReadResult> mListenerWeightHeight =
            new HealthResultHolder.ResultListener<ReadResult>() {
                @Override
                public void onResult(ReadResult result) {
                    Cursor c = null;
                    int height = 0;
                    int weight = 0;
                    double bmi = 0;
                    try {
                        c = result.getResultCursor();
                        if (c != null) {
                            while (c.moveToNext()) {
                                height = c.getInt(c.getColumnIndex(HealthConstants.Weight.WEIGHT));
                                weight = c.getInt(c.getColumnIndex(HealthConstants.Weight.HEIGHT));
                                double tmp = (double) height / 100;
                                bmi = (double) weight / (tmp * tmp);
                            }
                        }
                    } finally {
                        if (c != null) {
                            c.close();
                        }
                    }
                    profileFragment.drawHeightWeight(String.valueOf(height) + "-" +
                            String.valueOf(weight) + "-" + String.valueOf(bmi));
                }
            };

    private final HealthResultHolder.ResultListener<HealthDataResolver.ReadResult> mListenerStepCount =
            new HealthResultHolder.ResultListener<HealthDataResolver.ReadResult>() {
                @Override
                public void onResult(HealthDataResolver.ReadResult result) {
                    int count = 0;
                    Cursor c = null;

                    try {
                        c = result.getResultCursor();
                        if (c != null) {
                            while (c.moveToNext()) {
                                count = c.getInt(c.getColumnIndex(HealthConstants.StepCount.COUNT));
                                Log.d("LinhTh", String.valueOf(count));
                            }
                        }
                    } finally {
                        if (c != null) {
                            c.close();
                        }
                    }
                    profileFragment.drawStepCount(String.valueOf(count));
                }
            };

    private final HealthResultHolder.ResultListener<ReadResult> mListenerHeartRate =
            new HealthResultHolder.ResultListener<ReadResult>() {
                @Override
                public void onResult(ReadResult result) {
                    int count = 0;
                    Cursor c = null;
                    try {
                        c = result.getResultCursor();
                        if (c != null) {
                            while (c.moveToNext()) {
                                count = c.getInt(c.getColumnIndex(HealthConstants.HeartRate.HEART_RATE));
                            }
                        }
                    } finally {
                        if (c != null) {
                            c.close();
                        }
                    }
                    profileFragment.drawHeartRate(String.valueOf(count));
                }
            };

    private final HealthDataObserver mObserver = new HealthDataObserver(null) {

        // Update the step count when a change event is received
        @Override
        public void onChange(String dataTypeName) {
            Log.d("LinhTh", "Observer receives a data changed event");
            readStepCount();
            readWeightAndHeight();
            readHeartRate();
        }
    };

}
