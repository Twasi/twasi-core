package net.twasi.core.plugin.api.customcommands.structuredcommands;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.customcommands.TwasiCustomCommandEvent;
import net.twasi.core.plugin.api.customcommands.TwasiPluginCommand;
import net.twasi.core.plugin.api.customcommands.structuredcommands.subcommands.ISubCommands;
import net.twasi.core.plugin.api.customcommands.structuredcommands.subcommands.SubCommandCollection;
import net.twasi.core.plugin.api.customcommands.structuredcommands.subcommands.TwasiSubCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class StructuredPluginCommand extends TwasiPluginCommand implements ISubCommands {

    public StructuredPluginCommand(TwasiUserPlugin twasiUserPlugin) {
        super(twasiUserPlugin);
    }

    @Override
    protected final boolean execute(TwasiCustomCommandEvent event) {
        SubCommandCollection subCommands = getSubCommands();
        if (event.getArgs().size() > 0 && subCommands != null && subCommands.size() > 0) {
            String subCommand = event.getArgs().get(0);
            for (Class<? extends TwasiSubCommand> cmd : getSubCommands()) {
                try {
                    TwasiSubCommand sub = cmd.getDeclaredConstructor(TwasiCustomCommandEvent.class, ISubCommands.class).newInstance(event, this);
                    if (sub.getCommandName().equalsIgnoreCase(subCommand)) return sub.fire();
                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
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

    @Override
    public String getSyntaxKey() {
        return getCommandName() + ".syntax";
    }

    @Override
    public final TwasiUserPlugin getProvidingUserPlugin() {
        return twasiUserPlugin;
    }
}
