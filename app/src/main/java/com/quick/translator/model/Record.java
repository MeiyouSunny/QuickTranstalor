package com.quick.translator.model;

import java.io.Serializable;
import java.util.Objects;

public class Record implements Serializable {
    public Language fromLanguage;
    public Language toLanguage;
    public String fromText;
    public String toText;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Record)) return false;
        Record record = (Record) o;
        return fromLanguage.equals(record.fromLanguage) &&
                toLanguage.equals(record.toLanguage) &&
                fromText.equals(record.fromText) &&
                toText.equals(record.toText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromLanguage, toLanguage, fromText, toText);
    }
}
