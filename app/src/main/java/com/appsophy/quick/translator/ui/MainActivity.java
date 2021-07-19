package com.appsophy.quick.translator.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.google.android.material.navigation.NavigationView;
import com.appsophy.quick.translator.R;
import com.appsophy.quick.translator.util.SystemUtil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import likly.dollar.$;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawer;
    NavigationView mNaviView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mDrawer = findViewById(R.id.drawer_layout);
        mNaviView = findViewById(R.id.nav_view);

        mNaviView.setNavigationItemSelectedListener(this);

        initOCR();
//        requestPermissions();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.menu_share:
                SystemUtil.shareText(getApplicationContext(), getString(R.string.share_app));
                break;
            case R.id.menu_rate:
                RateDialog rateDialog = new RateDialog(this);
                rateDialog.showRateUs();
                break;
            case R.id.menu_feedback:
                SystemUtil.sendMail(this);
                break;
            case R.id.menu_policy:
                startActivity(new Intent(this, WebPageActivity.class));
                break;

        }
        mDrawer.closeDrawers();
        return false;
    }

    public void openMenu(View view) {
        mDrawer.open();
    }

    private void initOCR() {
        OCR.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                Log.e("获取成功", token);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(getApplicationContext(), "OCR认证成功！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(OCRError error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(getApplicationContext(), "OCR认证失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, getApplicationContext());
    }

    private void requestPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        boolean exitAlert = $.config().getBoolean("exitAlert", true);
        if (exitAlert) {
            RateDialog rateDialog = new RateDialog(this);
            rateDialog.showExitApp();
        } else {
            super.onBackPressed();
        }
    }

}