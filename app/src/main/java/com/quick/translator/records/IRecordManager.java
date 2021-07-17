package com.quick.translator.records;

import com.quick.translator.model.Record;

import java.util.List;

public interface IRecordManager {

    String KEY = "records";

    List<Record> getRecords();

    List<Record> insertRecord(Record record);

    List<Record> deleteRecord(Record record);

}
