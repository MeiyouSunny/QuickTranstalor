package com.quick.translator.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.quick.translator.model.Language;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import likly.dollar.$;

public class LanguageUtil {

    public List<Language> parseRegions(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open("lan_configs.json"), "UTF-8");
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            inputStreamReader.close();

            final String json = builder.toString();
            if (!TextUtils.isEmpty(json)) {
                return $.json().toList(json, Language.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
