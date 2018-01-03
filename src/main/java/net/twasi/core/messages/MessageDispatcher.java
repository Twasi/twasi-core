package net.twasi.core.messages;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Message.TwasiCommand;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.api.TwasiPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import net.twasi.core.plugin.api.events.TwasiMessageEvent;
import net.twasi.core.services.PluginManagerService;

import java.util.List;

public class MessageDispatcher {

    private TwasiInterface twasiInterface;

    public MessageDispatcher(TwasiInterface inf) {
        this.twasiInterface = inf;
    }

    public boolean dispatch(TwasiMessage msg) {
        if (msg.isCommand()) {
            TwasiCommand twasiCommand = msg.toCommand();

            List<TwasiPlugin> availablePlugins = PluginManagerService.getService().getByCommand(twasiCommand.getCommandName());

            for (TwasiPlugin plugin : availablePlugins) {
                plugin.onCommand(new TwasiCommandEvent(twasiCommand));
            }
        }
        List<TwasiPlugin> onMessagePlugins = PluginManagerService.getService().getMessagePlugins();

        for (TwasiPlugin plugin : onMessagePlugins) {
            try {
                plugin.onMessage(new TwasiMessageEvent(msg));
            } catch (Throwable e) {
                TwasiLogger.log.error("Exception while  executing onMessage of plugin " + plugin.getConfig().getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        return true;
    }

}
