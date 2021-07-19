package com.appsophy.quick.translator.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 夜雨飘零 on 2017/9/15.
 */

public class ToastUtil {
    private static Toast toast;

    public static void showToast(Context context,String text) {
        if (toast == null){
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }else {
            toast.setText(text);
        }
        toast.show();
    }
}
