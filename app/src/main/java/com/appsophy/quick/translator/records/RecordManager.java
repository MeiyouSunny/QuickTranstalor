package com.appsophy.quick.translator.records;

import android.text.TextUtils;

import com.appsophy.quick.translator.model.Record;

import java.util.ArrayList;
import java.util.List;

import likly.dollar.$;

public class RecordManager implements IRecordManager {

    @Override
    public List<Record> getRecords() {
        List<Record> records = new ArrayList<>();
        String json = $.config().getString(KEY, "");
        if (TextUtils.isEmpty(json))
            return records;
        records = $.json().toList(json, Record.class);
        return records;
    }

    @Override
    public List<Record> insertRecord(Record record) {
        // 先去重
        List<Record> records = deleteRecord(record);
        records.add(0, record);
        saveRecords(records);

        return records;
    }

    @Override
    public List<Record> deleteRecord(Record record) {
        List<Record> records = getRecords();
        if (records != null && records.size() > 0) {
            for (int i = 0; i < records.size(); i++) {
                if (record.equals(records.get(i))) {
                    records.remove(i);
                    break;
                }
            }
        }
        saveRecords(records);

        return records;
    }

    @Override
    public void clearRecords() {
        saveRecords(new ArrayList<Record>());
    }

    private void saveRecords(List<Record> records) {
        $.config().putString(KEY, $.json().toJson(records));
    }

}
