package com.quick.translator;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;

import likly.dollar.$;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        $.initialize(this);
        SpeechUtility.createUtility(getApplicationContext(), "appid=5618716d");
    }
}
