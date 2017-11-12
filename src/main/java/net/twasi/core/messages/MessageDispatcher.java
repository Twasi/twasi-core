package net.twasi.core.messages;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.models.Message.Command;
import net.twasi.core.models.Message.Message;
import net.twasi.core.plugin.api.TwasiPlugin;
import net.twasi.core.services.PluginManagerService;

import java.util.List;

public class MessageDispatcher {

    private TwasiInterface twasiInterface;

    public MessageDispatcher(TwasiInterface inf) {
        this.twasiInterface = inf;
    }

    public boolean dispatch(Message msg) {
        if (msg.isCommand()) {
            Command command = msg.toCommand();

            List<TwasiPlugin> availablePlugins = PluginManagerService.getService().getByCommand(command.getCommandName());

            if (availablePlugins.size() == 0) {
                return false;
            }

            for (TwasiPlugin plugin : availablePlugins) {
                plugin.onCommand(command);
            }

            List<TwasiPlugin> onMessagePlugins = PluginManagerService.getService().getMessagePlugins();

            for (TwasiPlugin plugin : onMessagePlugins) {
                plugin.onMessage(msg);
            }
        }
        return false;
    }

}
