package net.twasi.core.plugin.api;

public abstract class TwasiCustomCommand {

    private TwasiUserPlugin twasiUserPlugin;

    public TwasiCustomCommand(TwasiUserPlugin twasiUserPlugin) {
        this.twasiUserPlugin = twasiUserPlugin;
    }

    public abstract void process(TwasiCustomCommandEvent event);

    public abstract String getCommandName();

    protected String getTranslation(String key, Object... objects) {
        return this.twasiUserPlugin.getTranslation(key, objects);
    }

}
