package com.appsophy.quick.translator.util;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog {

    private ProgressDialog mLoadingDialog;

    public void showLoading(Context context) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new ProgressDialog(context);
            mLoadingDialog.setMessage("Loading,please wait...");
            mLoadingDialog.setCancelable(true);
        }
        mLoadingDialog.show();
    }

    public void dismiss() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

}
