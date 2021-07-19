package com.appsophy.quick.translator.ui.repeatview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import likly.view.repeat.ViewHolder;

public abstract class BaseViewHolder<B extends ViewDataBinding, T> extends ViewHolder<T> {

    public B bindRoot;

    @Override
    public View onCreateView(ViewGroup viewGroup) {
        bindRoot = DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()), getViewHolderLayout(),
                        viewGroup, false);
        return bindRoot.getRoot();

//        return LayoutInflater.from(viewGroup.getContext()).inflate(getViewHolderLayout(), viewGroup, false);
    }

    protected abstract int getViewHolderLayout();

    @Override
    protected void onViewCreated(View view) {
        super.onViewCreated(view);
//        ButterKnife.bind(this, view);
    }
}
