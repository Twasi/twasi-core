package net.twasi.core.plugin.api;

import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;

public abstract class TwasiCustomCommand {

    private TwasiUserPlugin twasiUserPlugin;

    public TwasiCustomCommand(TwasiUserPlugin twasiUserPlugin) {
        this.twasiUserPlugin = twasiUserPlugin;
    }

    public abstract void process(TwasiCustomCommandEvent event);

    public abstract String getCommandName();

    public String getFormattedCommandName(){
        return ServiceRegistry.get(ConfigService.class).getCatalog().bot.prefix  + getCommandName();
    }

    public boolean allowsTimer() {
        return false;
    }

    protected String getTranslation(String key, Object... objects) {
        return this.twasiUserPlugin.getTranslation(key, objects);
    }

}
