package com.appsophy.quick.translator.util;

import android.content.Context;

public class ResUtil {

    private Context mContext;

    public ResUtil(Context context) {
        mContext = context;
    }

    public int getDrawableId(String paramString) {
        return mContext.getResources().getIdentifier(paramString,
                "drawable", mContext.getPackageName());
    }

}
