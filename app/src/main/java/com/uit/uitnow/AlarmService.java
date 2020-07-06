package com.uit.uitnow;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class AlarmService extends IntentService {
    public static final String NOTIFICATION = "com.uit.uitnow";
    public AlarmService(){
        super("AlarmService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
