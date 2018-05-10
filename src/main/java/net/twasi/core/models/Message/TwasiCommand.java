package net.twasi.core.models.Message;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;

public class TwasiCommand extends TwasiMessage {
    public TwasiCommand(String message, MessageType type, TwitchAccount sender, TwasiInterface inf) {
        super(message, type, sender, inf);
    }

    public String getCommandName() {
        return message.split(" ")[0].substring(ServiceRegistry.get(ConfigService.class).getCatalog().bot.prefix.length()).toLowerCase();
    }
}
