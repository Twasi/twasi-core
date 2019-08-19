package net.twasi.core.database.models;

import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

@Entity(value = "queue-entries", noClassnameStored = true)
public class BetaCode extends BaseEntity {

    private String betaCode;
    private Date unlocked;
    private String userId;

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
}
