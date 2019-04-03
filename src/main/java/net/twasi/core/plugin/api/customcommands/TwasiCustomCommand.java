package net.twasi.core.plugin.api.customcommands;

import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;

import java.util.ArrayList;
import java.util.List;

public abstract class TwasiCustomCommand {

    public abstract void process(TwasiCustomCommandEvent event);

    public abstract String getCommandName();

    public final String getFormattedCommandName() {
        return ServiceRegistry.get(ConfigService.class).getCatalog().bot.prefix + getCommandName();
    }

    public abstract boolean allowsTimer();

    public abstract boolean allowsListing();

    public List<String> getAliases() {
        return new ArrayList<>();
    }

    public final List<String> getCommandNames() {
        List<String> list = new ArrayList<>(getAliases());
        list.add(getCommandName());
        return list;
    }

}
