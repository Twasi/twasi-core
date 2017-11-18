package net.twasi.core.database.models;

import net.twasi.core.database.Database;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class GlobalConfig {
    @Id
    private ObjectId id;
    private boolean isActivated;

    public GlobalConfig() {

    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public static GlobalConfig getDefault() {
        GlobalConfig config = new GlobalConfig();
        config.setActivated(true);
        Database.getStore().save(config);
        return config;
    }
}
