package net.twasi.core.models.Message;

import net.twasi.core.config.Config;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.interfaces.api.TwasiInterface;

public class TwasiCommand extends TwasiMessage {
    public TwasiCommand(String message, MessageType type, TwitchAccount sender, TwasiInterface inf) {
        super(message, type, sender, inf);
    }

    public String getCommandName() {
        return message.split(" ")[0].substring(Config.getCatalog().bot.prefix.length()).toLowerCase();
    }
}
