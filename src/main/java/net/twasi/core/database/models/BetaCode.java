package net.twasi.core.database.models;

import org.mongodb.morphia.annotations.Entity;

@Entity("betaCodes")
public class BetaCode extends BaseEntity {

    private String code;
    private boolean isActivated;
    private String userId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
