package com.appsophy.quick.translator.adapter;

import android.view.View;

import com.appsophy.quick.translator.R;
import com.appsophy.quick.translator.databinding.ItemRecordBinding;
import com.appsophy.quick.translator.event.EventUtil;
import com.appsophy.quick.translator.model.Record;
import com.appsophy.quick.translator.records.RecordManager;
import com.appsophy.quick.translator.ui.repeatview.BaseViewHolder;
import com.appsophy.quick.translator.util.SystemUtil;
import com.appsophy.quick.translator.util.TTSPlayer;

import likly.dollar.$;

public class AdapterRecord extends BaseViewHolder<ItemRecordBinding, Record> {

    @Override
    protected void onBindData(Record record) {
        bindRoot.setRecord(record);
        bindRoot.executePendingBindings();

        bindRoot.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bindRoot.layoutTools.getVisibility() == View.VISIBLE) {
                    bindRoot.layoutTools.setVisibility(View.INVISIBLE);
                    bindRoot.expand.setImageResource(R.drawable.ic_arrow_down);
                } else {
                    bindRoot.layoutTools.setVisibility(View.VISIBLE);
                    bindRoot.expand.setImageResource(R.drawable.ic_arrow_up);
                }
            }
        });

        bindRoot.speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSPlayer.getInstance(getContext().getApplicationContext()).playTTS(record.toText);
            }
        });

        bindRoot.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtil.copyToClipboard(getContext().getApplicationContext(), record.toText);
                $.toast().text(R.string.copied).show();
            }
        });

        bindRoot.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtil.shareText(getContext().getApplicationContext(), record.toText);
            }
        });

        bindRoot.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RecordManager().deleteRecord(record);
                EventUtil.sendRefreshRecordsEvent();
                $.toast().text(R.string.deleted).show();
            }
        });
    }

    @Override
    protected int getViewHolderLayout() {
        return R.layout.item_record;
    }

    @Override
    protected void onViewCreated(View view) {
        super.onViewCreated(view);
    }

}
