package net.twasi.core.plugin;

import net.twasi.core.database.models.User;
import net.twasi.core.models.Message.TwasiCommand;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.customcommands.TwasiCustomCommandEvent;
import net.twasi.core.plugin.api.customcommands.TwasiDependencyCommand;
import net.twasi.core.translations.TwasiTranslation;
import org.jetbrains.annotations.NotNull;

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

    public final String getTranslation(User user, String key, Object... objects) {
        return twasiTranslation.getTranslation(user, key, objects);
    }

    public final String getRandomTranslation(User user, String key, Object... objects) {
        return twasiTranslation.getRandomTranslation(user, key, objects);
    }

    @NotNull
    public List<TwasiDependencyCommand> getCommands() {
        return new ArrayList<>();
    }

    public final void handleCommand(TwasiCommand twasiCommand) {
        getCommands().stream().filter(cmd -> {
            if (cmd.getCommandName().equalsIgnoreCase(twasiCommand.getCommandName()))
                return true;
            else
                return cmd.getAliases().stream().map(String::toLowerCase).collect(Collectors.toList()).contains(twasiCommand.getCommandName().toLowerCase());
        }).collect(Collectors.toList()).forEach(handler -> handler.process(new TwasiCustomCommandEvent(twasiCommand)));
    }
}
