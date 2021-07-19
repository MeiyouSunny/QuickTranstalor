package com.appsophy.quick.translator.adapter;

import com.appsophy.quick.translator.R;
import com.appsophy.quick.translator.databinding.ItemLanguageBinding;
import com.appsophy.quick.translator.model.Language;
import com.appsophy.quick.translator.ui.repeatview.BaseViewHolder;
import com.appsophy.quick.translator.util.ResUtil;

public class AdapterLanguage extends BaseViewHolder<ItemLanguageBinding, Language> {
    ResUtil resUtil;

    @Override
    protected void onBindData(Language language) {
        if (resUtil == null)
            resUtil = new ResUtil(getContext());

        bindRoot.setResUtil(resUtil);
        bindRoot.setLanguage(language);
        bindRoot.executePendingBindings();
    }

    @Override
    protected int getViewHolderLayout() {
        return R.layout.item_language;
    }

}
