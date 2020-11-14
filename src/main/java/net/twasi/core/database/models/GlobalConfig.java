package net.twasi.core.database.models;

import org.mongodb.morphia.annotations.Entity;

@Entity("globalConfigs")
public class GlobalConfig {

    private boolean isActivated;
    private Language language;

    public GlobalConfig() {

    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    static GlobalConfig getDefault() {
        GlobalConfig config = new GlobalConfig();
        config.setActivated(true);
        config.setLanguage(Language.DE_DE);
        return config;
    }
}
