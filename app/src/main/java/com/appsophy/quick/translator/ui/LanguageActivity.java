package com.appsophy.quick.translator.ui;

import android.content.Intent;
import android.os.Bundle;

import com.appsophy.quick.translator.R;
import com.appsophy.quick.translator.adapter.AdapterLanguage;
import com.appsophy.quick.translator.model.Language;
import com.appsophy.quick.translator.util.LanguageUtil;

import java.util.List;

import likly.view.repeat.OnHolderClickListener;
import likly.view.repeat.RepeatView;

public class LanguageActivity extends BaseTitleActivity implements OnHolderClickListener<AdapterLanguage> {

    RepeatView repeatView;

    @Override
    protected int layoutId() {
        return R.layout.activity_language;
    }

    @Override
    protected int title() {
        return R.string.select_language;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repeatView = findViewById(R.id.repeatView);
        repeatView.onClick(this);

        initLanguageData();
    }

    private void initLanguageData() {
        List<Language> languages = LanguageUtil.parseRegions(getApplicationContext());
        if (languages != null && languages.size() > 0) {
            boolean isTo = getIntent().getBooleanExtra("to", false);
            if (isTo)
                languages.remove(0);
            repeatView.viewManager().bind(languages);
        }
    }

    @Override
    public void onHolderClick(AdapterLanguage adapterLanguage) {
        Intent data = new Intent();
        data.putExtra("language", adapterLanguage.getData());
        setResult(RESULT_OK, data);
        finish();
    }

}