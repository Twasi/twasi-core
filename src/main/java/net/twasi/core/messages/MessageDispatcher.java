package net.twasi.core.messages;

import com.google.gson.Gson;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.messages.internal.InternalCommandHandler;
import net.twasi.core.models.Message.TwasiCommand;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.TwasiDependency;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import net.twasi.core.plugin.api.events.TwasiMessageEvent;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.PluginManagerService;

import java.util.List;
import java.util.stream.Collectors;

public class MessageDispatcher {

    private transient TwasiInterface twasiInterface;
    private InternalCommandHandler internalCommandHandler;

    public MessageDispatcher(TwasiInterface inf) {
        this.twasiInterface = inf;

        internalCommandHandler = new InternalCommandHandler();
    }

    public boolean dispatch(TwasiMessage msg) {
        if (msg.isCommand()) {
            TwasiCommand twasiCommand = msg.toCommand();

            TwasiLogger.log.debug("Executing message as command (sent by " + msg.getSender().getDisplayName() + ")");

            if (!internalCommandHandler.handle(twasiCommand)) {

                List<TwasiUserPlugin> availablePlugins = twasiInterface.getByCommand(twasiCommand.getCommandName());

                TwasiLogger.log.debug("Handling plugins: " + new Gson().toJson(availablePlugins.stream().map(plugin -> plugin.getCorePlugin().getClass().toString()).collect(Collectors.toList())));

                for (TwasiUserPlugin plugin : availablePlugins) {
                    Thread commandExecutionThread = new Thread(() -> {
                        try {
                            plugin.onCommand(new TwasiCommandEvent(twasiCommand));
                        } catch (Throwable e) {
                            TwasiLogger.log.error("Exception while executing onCommand of plugin " + plugin.getClass() + ": " + e.getMessage());
                            TwasiLogger.log.trace(e);
                            e.printStackTrace();
                        }
                    });
                    commandExecutionThread.setDaemon(true);
                    commandExecutionThread.start();
                }
            }

            List<TwasiDependency> dependencies = ServiceRegistry.get(PluginManagerService.class).getDependencies();
            for (TwasiDependency dependency : dependencies) {
                Thread commandExecutionThread = new Thread(() -> dependency.handleCommand(twasiCommand));
                commandExecutionThread.setDaemon(true);
                commandExecutionThread.start();
            }
        }
        List<TwasiUserPlugin> onMessagePlugins = twasiInterface.getMessagePlugins();

        for (TwasiUserPlugin plugin : onMessagePlugins) {
            Thread commandExecutionThread = new Thread(() -> {
                try {
                    plugin.onMessage(new TwasiMessageEvent(msg));
                } catch (Throwable e) {
                    TwasiLogger.log.error("Exception while  executing onMessage of plugin " + plugin.getClass() + ": " + e.getMessage());
                    TwasiLogger.log.trace(e);
                    e.printStackTrace();
                }
            });
            commandExecutionThread.setDaemon(true);
            commandExecutionThread.start();
        }
        return true;
    }

}
