package net.twasi.core.plugin.api.customcommands;

import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.translations.renderer.TranslationRenderer;

public abstract class TwasiPluginCommand extends TwasiCustomCommand {

    protected TwasiUserPlugin twasiUserPlugin;

    public TwasiPluginCommand(TwasiUserPlugin twasiUserPlugin) {
        this.twasiUserPlugin = twasiUserPlugin;
    }

    @Override
    public boolean allowsTimer() {
        return true;
    }

    @Override
    public boolean allowsListing() {
        return true;
    }

    @Deprecated
    protected final String getTranslation(String key, Object... objects) {
        return this.twasiUserPlugin.getTranslation(key, objects);
    }

    @Deprecated
    protected final String getRandomTranslation(String key, Object... objects) {
        return this.twasiUserPlugin.getRandomTranslation(key, objects);
    }

    @Override
    protected final TranslationRenderer getTranslationRenderer(){
        return TranslationRenderer.getInstance(twasiUserPlugin, "");
    }

    public TwasiUserPlugin getProvidingUserPlugin() {
        return twasiUserPlugin;
    }
}
