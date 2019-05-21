package net.twasi.core.database.models;

import java.text.SimpleDateFormat;

public enum Language {
    EN_GB("English (GB)", "dd/MM/yyyy"),
    EN_US("English (US)", "MM/dd/yyyy"),
    DE_DE("Deutsch", "dd.MM.yyyy");

    private final String languageName;
    private final SimpleDateFormat dateFormat;

    Language(String languageName, String simpleDateFormat) {
        this.languageName = languageName;
        this.dateFormat = new SimpleDateFormat(simpleDateFormat);
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public Units getUnits() {
        return Units.byLanguage(this);
    }

    public String getLanguageName() {
        return languageName;
    }
}
