package net.twasi.core.plugin.api.customcommands.structuredcommands.subcommands;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.api.customcommands.TwasiCustomCommandEvent;
import net.twasi.core.plugin.api.customcommands.structuredcommands.TwasiStructuredCommandEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class TwasiSubCommand implements ISubCommands {

    private final TwasiCustomCommandEvent event;
    private ISubCommands parent;

    public TwasiSubCommand(TwasiCustomCommandEvent event, ISubCommands parent) {
        this.event = event;
        this.parent = parent;
    }

    public final boolean fire() {
        if (requirePermissionKey() != null && !event.getStreamer().getUser().hasPermission(event.getSender(), requirePermissionKey()))
            return false;

        if (event.getArgs().size() > 0 && getSubCommands() != null && getSubCommands().size() > 0) {
            String subCommand = event.getArgs().get(0);
            for (Class<? extends TwasiSubCommand> cmd : getSubCommands()) {
                try {
                    TwasiSubCommand sub = cmd.getDeclaredConstructor(TwasiCustomCommandEvent.class, ISubCommands.class).newInstance(event, this);
                    if (sub.getCommandName().equalsIgnoreCase(subCommand)) return sub.fire();
                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
                    // Should be never thrown
                } catch (Exception e) {
                    TwasiLogger.log.debug("Error while trying to execute a subcommand of " +
                            String.join(".", getParentCommandNames() + "." +
                                    getCommandName() + ": " + e.getMessage()));
                }
            }
        }
        return handle(new TwasiStructuredCommandEvent(event));
    }

    protected boolean handle(TwasiStructuredCommandEvent event) {
        event.reply(event.getRenderer().render(getSyntaxKey()));
        return false;
    }

    @Override
    public SubCommandCollection getSubCommands() {
        return SubCommandCollection.EMPTY();
    }

    @Override
    public List<String> getParentCommandNames() {
        List<String> subCommands = new ArrayList<>(parent.getParentCommandNames());
        subCommands.add(parent.getCommandName());
        return subCommands;
    }

    public abstract String getCommandName();

}
