package net.twasi.core.database.models;

import net.twasi.core.database.Database;
import org.mongodb.morphia.annotations.Entity;

@Entity("globalConfigs")
public class GlobalConfig {

    private boolean isActivated;
    private String language;

    public GlobalConfig() {

    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public static GlobalConfig getDefault() {
        GlobalConfig config = new GlobalConfig();
        config.setActivated(true);
        config.setLanguage("DE_DE");
        Database.getStore().save(config);
        return config;
    }
}
