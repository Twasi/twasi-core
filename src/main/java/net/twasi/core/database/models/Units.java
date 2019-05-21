package net.twasi.core.database.models;

public class Units {

    public final String SECOND;
    public final String SECONDS;
    public final String MINUTE;
    public final String MINUTES;
    public final String HOUR;
    public final String HOURS;
    public final String DAY;
    public final String DAYS;
    public final String WEEK;
    public final String WEEKS;
    public final String MONTH;
    public final String MONTHS;
    public final String YEAR;
    public final String YEARS;

    private Units(String SECOND, String SECONDS, String MINUTE, String MINUTES, String HOUR, String HOURS, String DAY, String DAYS, String WEEK, String WEEKS, String MONTH, String MONTHS, String YEAR, String YEARS) {
        this.SECOND = SECOND;
        this.SECONDS = SECONDS;
        this.MINUTE = MINUTE;
        this.MINUTES = MINUTES;
        this.HOUR = HOUR;
        this.HOURS = HOURS;
        this.DAY = DAY;
        this.DAYS = DAYS;
        this.WEEK = WEEK;
        this.WEEKS = WEEKS;
        this.MONTH = MONTH;
        this.MONTHS = MONTHS;
        this.YEAR = YEAR;
        this.YEARS = YEARS;
    }

    public static Units byLanguage(Language language) {
        if (language.equals(Language.DE_DE)) {
            return new Units(
                    "Sekunde", "Sekunden",
                    "Minute", "Minuten",
                    "Stunde", "Stunden",
                    "Tag", "Tage",
                    "Woche", "Wochen",
                    "Monat", "Monate",
                    "Jahr", "Jahre"
            );
        } else {
            return new Units(
                    "second", "seconds",
                    "minute", "minutes",
                    "hour", "hours",
                    "day", "days",
                    "week", "weeks",
                    "month", "months",
                    "year", "years"
            );
        }
    }
}
