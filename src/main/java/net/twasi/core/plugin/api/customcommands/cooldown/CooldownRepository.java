package net.twasi.core.plugin.api.customcommands.cooldown;

import net.twasi.core.database.lib.Repository;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;

public class CooldownRepository extends Repository<CooldownEntity> {

    public CooldownEntity getCooldownOrCreate(User user, String twitchId, String command) {
        CooldownEntity entity = store.createQuery(CooldownEntity.class)
                .field("user").equal(user)
                .field("senderTwitchId").equal(twitchId)
                .field("commandName").equal(command.toLowerCase())
                .get();
        if (entity == null) {
            entity = new CooldownEntity(user, command, twitchId);
            add(entity);
        }
        return entity;
    }

    public CooldownEntity getCooldownOrCreate(User user, TwitchAccount account, String command) {
        return getCooldownOrCreate(user, account.getTwitchId(), command);
    }

}
