package com.appsophy.quick.translator.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.kyleduo.switchbutton.SwitchButton;
import com.appsophy.quick.translator.R;
import com.appsophy.quick.translator.event.EventUtil;
import com.appsophy.quick.translator.records.RecordManager;
import com.appsophy.quick.translator.util.SystemUtil;

import likly.dollar.$;

public class SettingsActivity extends BaseTitleActivity {

    SwitchButton mSwitchAutoCopy, mSwitchAutoSpeak;

    @Override
    protected int layoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected int title() {
        return R.string.menu_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSwitchAutoCopy = findViewById(R.id.switchAutoCopy);
        mSwitchAutoSpeak = findViewById(R.id.switchAutoSpeak);

        mSwitchAutoCopy.setChecked(SystemUtil.getAutoCopyFlag());
        mSwitchAutoSpeak.setChecked(SystemUtil.getAutoSpeakFlag());

        mSwitchAutoCopy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SystemUtil.saveAutoCopyFlag(isChecked);
            }
        });
        mSwitchAutoSpeak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SystemUtil.saveAutoSpeakFlag(isChecked);
            }

        });

        findViewById(R.id.clearHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RecordManager().clearRecords();
                EventUtil.sendRefreshRecordsEvent();
                $.toast().text(R.string.cleared).show();
            }
        });
    }

}