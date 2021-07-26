package com.appsophy.quick.translator;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.iflytek.cloud.SpeechUtility;

import likly.dollar.$;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        $.initialize(this);
        SpeechUtility.createUtility(getApplicationContext(), "appid=5618716d");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.e("MobileAds", "onInitializationComplete");
            }
        });
    }
}
