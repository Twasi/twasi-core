package net.twasi.core.plugin.api.customcommands.cooldown;

import jdk.nashorn.internal.ir.annotations.Reference;
import net.twasi.core.database.models.BaseEntity;
import net.twasi.core.database.models.User;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

public class CooldownEntity extends BaseEntity {

    @Reference
    private User user;

    private String commandName;
    private String senderTwitchId;

    private Date expiration;

    public CooldownEntity(User user, String commandName, String senderTwitchId) {
        this.user = user;
        this.commandName = commandName.toLowerCase();
        this.senderTwitchId = senderTwitchId;
        expiration = null;
    }

    public CooldownEntity() {
    }

    public CooldownEntity setCooldown(Duration cooldown) {
        Date now = Calendar.getInstance().getTime();
        now.setTime(now.getTime() + cooldown.toMillis());
        this.expiration = now;
        return this;
    }

    public boolean hasCooldown() {
        return expiration != null && expiration.getTime() > Calendar.getInstance().getTimeInMillis();
    }

    public User getUser() {
        return user;
    }

    public String getCommandName() {
        return commandName.toLowerCase();
    }

    public String getSenderTwitchId() {
        return senderTwitchId;
    }

    public Date getExpiration() {
        return expiration;
    }
}
