package net.twasi.core.graphql.model;

import net.twasi.core.database.models.User;
import net.twasi.core.plugin.PluginConfig;
import net.twasi.core.services.providers.InstanceManagerService;

import java.util.List;

public class PluginDetailsDTO {
    private final String name;
    private final String id;
    private final String description;
    private final String author;
    private final String version;
    private final List<String> commands;
    private final List<String> permissions;

    private final boolean isInstalled;

    public PluginDetailsDTO(PluginConfig config, boolean isInstalled) {
        this(config, null, isInstalled);
    }

    public PluginDetailsDTO(PluginConfig config, User user, boolean isInstalled) {
        if (user != null) {
            name = config.getLocalizedName(user);
            description = config.getLocalizedDescription(user);
        } else {
            name = config.getName();
            description = config.getDescription();
        }

        author = config.getAuthor();
        version = config.getVersion();

        commands = config.getCommands();
        permissions = config.getPermissions();

        this.isInstalled = isInstalled;
        this.id = config.getName();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public String getId() {
        return id;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public long getInstallations() {
        return InstanceManagerService.get()
                .getInterfaces().stream()
                .filter(i -> i.getPlugins().stream().anyMatch(
                        pl -> pl.getCorePlugin().getDescription().name.equalsIgnoreCase(this.name)
                )).count();
    }
}
