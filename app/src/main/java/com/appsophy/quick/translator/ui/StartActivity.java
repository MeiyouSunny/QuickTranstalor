package com.appsophy.quick.translator.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;

import com.appsophy.quick.translator.R;
import com.appsophy.quick.translator.util.InterstitialAdLoader;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    InterstitialAdLoader mAdLoader;
    boolean mtAdShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        mAdLoader = new InterstitialAdLoader();

        start();
    }

    private void start() {
        mAdLoader.loadAd(this, new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                gotoHome();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                mtAdShown = true;
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                mtAdShown = false;
                gotoHome();
            }

        });

        startDelayGotoHome();
    }

    private void startDelayGotoHome() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mtAdShown)
                    return;
                gotoHome();
            }
        }, 8000);
    }

    private boolean mHasGotoHome;

    private void gotoHome() {
        if (mHasGotoHome)
            return;
        mHasGotoHome = true;
        startActivity(new Intent(StartActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdLoader = null;
    }
}