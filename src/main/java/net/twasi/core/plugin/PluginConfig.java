package net.twasi.core.plugin;

import java.util.List;

public class PluginConfig {

    public String name;
    public String description;
    public String author;
    public String version;
    public String main;

    public List<String> commands;

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

    public String getMain() {
        return main;
    }

    public List<String> getCommands() {
        return commands;
    }
}
