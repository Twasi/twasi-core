package net.twasi.core.database.models;

import java.text.SimpleDateFormat;

public enum Language {
    EN_GB("dd/MM/yyyy"),
    EN_US("MM/dd/yyyy"),
    DE_DE("dd.MM.yyyy");

    private final SimpleDateFormat dateFormat;

    Language(String simpleDateFormat){
        this.dateFormat = new SimpleDateFormat(simpleDateFormat);
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }
}
