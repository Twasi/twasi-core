package net.twasi.core.messages;

import com.google.gson.Gson;
import net.twasi.core.database.models.User;
import net.twasi.core.events.CommandExecutedEvent;
import net.twasi.core.events.TwasiEventHandler;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.messages.internal.InternalCommandHandler;
import net.twasi.core.models.Message.TwasiCommand;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import net.twasi.core.plugin.api.events.TwasiMessageEvent;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageDispatcher {

    private static Map<TwasiEventHandler<CommandExecutedEvent>, ObjectId> handlers = new HashMap<>();

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
                            handlers.entrySet().stream()
                                    .filter(e -> e.getValue() == null || e.getValue().equals(twasiInterface.getStreamer().getUser().getId()))
                                    .forEach(e -> {
                                        try {
                                            e.getKey().on(new CommandExecutedEvent(twasiCommand, plugin));
                                        } catch(Throwable ignored){
                                        }
                                    });
                        } catch (Throwable e) {
                            TwasiLogger.log.error("Exception while executing onCommand of plugin " + plugin.getClass() + ": " + e.getMessage(), e);
                        }
                    });
                    commandExecutionThread.setDaemon(true);
                    commandExecutionThread.start();
                }
            }
        }
        List<TwasiUserPlugin> onMessagePlugins = twasiInterface.getMessagePlugins();

        for (TwasiUserPlugin plugin : onMessagePlugins) {
            Thread commandExecutionThread = new Thread(() -> {
                try {
                    plugin.onMessage(new TwasiMessageEvent(msg));
                } catch (Throwable e) {
                    TwasiLogger.log.error("Exception while  executing onMessage of plugin " + plugin.getClass() + ": " + e.getMessage(), e);
                }
            });
            commandExecutionThread.setDaemon(true);
            commandExecutionThread.start();
        }
        return true;
    }

    public static void registerCommandExecutedEventHandler(TwasiEventHandler<CommandExecutedEvent> handler){
        handlers.put(handler, null);
    }

    public static void registerCommandExecutedEventHandler(User target, TwasiEventHandler<CommandExecutedEvent> handler){
        handlers.put(handler, target.getId());
    }

}
