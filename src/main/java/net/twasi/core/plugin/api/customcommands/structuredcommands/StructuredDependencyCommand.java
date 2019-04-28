package net.twasi.core.plugin.api.customcommands.structuredcommands;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.TwasiDependency;
import net.twasi.core.plugin.api.customcommands.TwasiCustomCommandEvent;
import net.twasi.core.plugin.api.customcommands.TwasiDependencyCommand;
import net.twasi.core.plugin.api.customcommands.structuredcommands.subcommands.ISubCommands;
import net.twasi.core.plugin.api.customcommands.structuredcommands.subcommands.SubCommandCollection;
import net.twasi.core.plugin.api.customcommands.structuredcommands.subcommands.TwasiSubCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class StructuredDependencyCommand extends TwasiDependencyCommand implements ISubCommands {

    protected StructuredDependencyCommand(TwasiDependency twasiDependency) {
        super(twasiDependency);
    }

    @Override
    protected final boolean execute(TwasiCustomCommandEvent event) {
        if (event.getArgs().size() > 0 && getSubCommands() != null && getSubCommands().size() > 0) {
            String subCommand = event.getArgs().get(0);
            for (Class<? extends TwasiSubCommand> cmd : getSubCommands()) {
                try {
                    TwasiSubCommand sub = cmd.getDeclaredConstructor(TwasiCustomCommandEvent.class, ISubCommands.class).newInstance(new TwasiStructuredCommandEvent(event), this);
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
        return handle(event);
    }

    protected boolean handle(TwasiCustomCommandEvent event) {
        event.reply(event.getRenderer().render(getSyntaxKey()));
        return false;
    }

    public abstract String getCommandName();

    @Override
    public abstract SubCommandCollection getSubCommands();

    @Override
    public final List<String> getParentCommandNames() {
        return new ArrayList<>();
    }
}
