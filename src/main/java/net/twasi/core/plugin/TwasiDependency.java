package net.twasi.core.plugin;

import net.twasi.core.database.models.User;
import net.twasi.core.models.Message.TwasiCommand;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.TwasiVariable;
import net.twasi.core.plugin.api.customcommands.TwasiDependencyCommand;
import net.twasi.core.translations.TwasiTranslation;
import net.twasi.core.translations.renderer.TranslationRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TwasiDependency extends TwasiPlugin {
    private TwasiTranslation twasiTranslation = new TwasiTranslation(getClassLoader());

    public Class<? extends TwasiUserPlugin> getUserPluginClass() {
        return null;
    }

    @Override
    public final boolean isDependency() {
        return true;
    }

    @Deprecated
    public final String getTranslation(User user, String key, Object... objects) {
        return twasiTranslation.getTranslation(user, key, objects);
    }

    @Deprecated
    public final String getRandomTranslation(User user, String key, Object... objects) {
        return twasiTranslation.getRandomTranslation(user, key, objects);
    }

    public final TranslationRenderer getTranslationRenderer(){
        return TranslationRenderer.getInstance(this);
    }

    public List<TwasiDependencyCommand> getCommands() {
        return new ArrayList<>();
    }

    public List<TwasiVariable> getVariables() {
        return new ArrayList<>();
    }

    public final void handleCommand(TwasiCommand twasiCommand) {
        getCommands().stream().filter(cmd -> {
            if (cmd.getCommandName().equalsIgnoreCase(twasiCommand.getCommandName()))
                return true;
            else
                return cmd.getAliases().stream().map(String::toLowerCase).collect(Collectors.toList()).contains(twasiCommand.getCommandName().toLowerCase());
        }).collect(Collectors.toList()).forEach(handler -> handler.processInternal(twasiCommand, getClassLoader()));
    }
}
