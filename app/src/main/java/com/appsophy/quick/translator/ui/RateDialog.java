package com.appsophy.quick.translator.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.appsophy.quick.translator.R;
import com.appsophy.quick.translator.util.SystemUtil;

import androidx.appcompat.app.AlertDialog;
import likly.dollar.$;

public class RateDialog {

    private Context mContext;
    private int mRateScore = 5;
    private int mTitle, mContent;
    private boolean mExit;

    public RateDialog(Context context) {
        mContext = context;
    }

    public void showRateUs() {
        mTitle = R.string.menu_rate;
        mContent = R.string.rate_us;
        show();
    }

    public void showExitApp() {
        mExit = true;
        mTitle = R.string.exit_app;
        mContent = R.string.rate_us;
        show();
    }

    private void show() {
        View rateView = LayoutInflater.from(mContext).inflate(R.layout.layout_rate, null);
        RatingBar ratingBar = rateView.findViewById(R.id.rate);
        EditText etInput = rateView.findViewById(R.id.etRateInput);
        TextView content = rateView.findViewById(R.id.content);
        content.setText(mContent);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mRateScore = (int) rating;
                if (mRateScore <= 3) {
                    etInput.setVisibility(View.VISIBLE);
                } else {
                    etInput.setVisibility(View.INVISIBLE);
                }
            }
        });

        Dialog dialog = new AlertDialog.Builder(mContext).setTitle(mTitle)
                .setView(rateView)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mExit) {
                                    $.config().putBoolean("exitAlert", false);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    return;
                                }
                                if (mRateScore > 3) {
                                    SystemUtil.launchAppDetail(mContext);
                                } else {
                                    $.config().putString("rate", etInput.getText().toString());
                                }
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.show();
    }

}
