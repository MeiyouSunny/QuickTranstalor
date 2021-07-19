package com.appsophy.quick.translator.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import likly.dollar.$;

public class SystemUtil {

    public static void copyToClipboard(Context context, String text) {
        ClipboardManager cm = (ClipboardManager) context.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", text);
        cm.setPrimaryClip(mClipData);
    }

    public static void shareText(Context context, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        context.startActivity(shareIntent);
    }

    public static void sendMail(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setPackage("com.android.email");
        intent.setType("email/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"quicktranslator@gmail.com"});
        context.startActivity(Intent.createChooser(intent, "Feedback"));
    }

    /**
     * 启动到应用商店app详情界面
     */
    public static void launchAppDetail(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getAutoCopyFlag() {
        return $.config().getBoolean("AutoCopy", false);
    }

    public static boolean getAutoSpeakFlag() {
        return $.config().getBoolean("AutoSpeak", false);
    }

    public static void saveAutoCopyFlag(boolean flag) {
        $.config().putBoolean("AutoCopy", flag);
    }

    public static void saveAutoSpeakFlag(boolean flag) {
        $.config().putBoolean("AutoSpeak", flag);
    }

}
