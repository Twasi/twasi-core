package net.twasi.core.database.models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

@Entity(value = "queue-entries", noClassnameStored = true)
public class BetaCode extends BaseEntity {
    private String betaCode;
    private Date unlocked;
    private String userId;
    private String email;
    private Date joined;
    private ObjectId objectId;

    public String getBetaCode() {
        return betaCode;
    }

    public void setBetaCode(String code) {
        this.betaCode = code;
    }

    public boolean isActivated() {
        return unlocked != null;
    }

    public Date getUnlocked() {
        return unlocked;
    }

    public void setUnlocked(Date unlocked) {
        this.unlocked = unlocked;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getJoined() {
        return joined;
    }

    public void setJoined(Date joined) {
        this.joined = joined;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }
}
