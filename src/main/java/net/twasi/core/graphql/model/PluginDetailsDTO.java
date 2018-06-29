package net.twasi.core.graphql.model;

import net.twasi.core.plugin.PluginConfig;

import java.util.List;

public class PluginDetailsDTO {
    private final String name;
    private final String description;
    private final String author;
    private final String version;
    private final List<String> commands;
    private final List<String> permissions;

    private final boolean isInstalled;

    public PluginDetailsDTO(PluginConfig config, boolean isInstalled) {
        name = config.getName();
        description = config.getDescription();
        author = config.getAuthor();
        version = config.getVersion();

        commands = config.getCommands();
        permissions = config.getPermissions();

        this.isInstalled = isInstalled;
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

    public boolean isInstalled() {
        return isInstalled;
    }
}
