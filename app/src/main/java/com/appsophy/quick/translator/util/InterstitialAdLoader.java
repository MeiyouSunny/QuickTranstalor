package com.appsophy.quick.translator.util;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import androidx.annotation.NonNull;

/**
 * 插屏公告
 */
public class InterstitialAdLoader {
    // "ca-app-pub-3940256099942544/1033173712"

    private final String[] AD_IDS = {
            "ca-app-pub-8991501814540546/8676949958",
            "ca-app-pub-8991501814540546/4609712435",
            "ca-app-pub-8991501814540546/3887657705"};

    private Activity mActivity;
    private InterstitialAd mInterstitialAd;
    private FullScreenContentCallback mCallback;
    private int mAdIndex = 0;
    private boolean mIsLoading;

    public void loadAd(Activity activity) {
        loadAd(activity, null);
    }

    public void loadAd(Activity activity, FullScreenContentCallback callback) {
        mActivity = activity;
        mCallback = callback;
        startLoad();
    }

    private void startLoad() {
        if(mIsLoading)
            return;

        mIsLoading = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(mActivity, AD_IDS[mAdIndex], adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mIsLoading = false;
                        mInterstitialAd = interstitialAd;

                        if (mCallback != null)
                            interstitialAd.setFullScreenContentCallback(mCallback);
                        else
                            setFullScreenContentCallback(mInterstitialAd);

                        mInterstitialAd.show(mActivity);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mIsLoading = false;
                        mInterstitialAd = null;
                        if (mAdIndex < 2) {
                            mAdIndex++;
                            startLoad();
                        }
                    }
                });
    }

    private void setFullScreenContentCallback(InterstitialAd interstitialAd) {
        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when fullscreen content is dismissed.
                Log.d("TAG", "The ad was dismissed.");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when fullscreen content failed to show.
                Log.d("TAG", "The ad failed to show.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when fullscreen content is shown.
                // Make sure to set your reference to null so you don't
                // show it a second time.
                mInterstitialAd = null;
                Log.d("TAG", "The ad was shown.");
            }
        });
    }

}
