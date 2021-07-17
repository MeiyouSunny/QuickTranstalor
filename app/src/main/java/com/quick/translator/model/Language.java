package com.quick.translator.model;

import java.io.Serializable;
import java.util.Objects;

public class Language implements Serializable {

    public String language;
    public String codeBaidu;
    public String codeIFly;
    public String icon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Language)) return false;
        Language language1 = (Language) o;
        return language.equals(language1.language) &&
                codeBaidu.equals(language1.codeBaidu) &&
                codeIFly.equals(language1.codeIFly) &&
                icon.equals(language1.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(language, codeBaidu, codeIFly, icon);
    }
}
