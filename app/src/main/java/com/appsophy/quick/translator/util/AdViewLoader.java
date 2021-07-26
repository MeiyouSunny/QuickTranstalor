package com.appsophy.quick.translator.util;

import android.content.Context;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import androidx.annotation.NonNull;

public class AdViewLoader {

    // "ca-app-pub-3940256099942544/6300978111"

    private final String[] AD_IDS = {
            "ca-app-pub-8991501814540546/1453066057",
            "ca-app-pub-8991501814540546/6449052494",
            "ca-app-pub-8991501814540546/4334665670"};

    private Context mContext;
    private int mAdIndex = 0;
    private FrameLayout mContainer;
    private AdView mAdView;

    public AdViewLoader(Context context, FrameLayout container) {
        mContext = context;
        mContainer = container;
    }

    public void loadAd() {
        if (mContainer.getChildCount() == 1)
            mContainer.removeAllViews();

        mAdView = new AdView(mContext);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(AD_IDS[mAdIndex]);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                if (mAdIndex < 2) {
                    mAdIndex++;
                    loadAd();
                }
            }
        });
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
        mContainer.addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

}
