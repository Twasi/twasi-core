package net.twasi.core.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.twasi.core.database.models.User;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PluginConfig {

    public String name;
    public String description;
    public String author;
    public String version;
    public String main;
    public String helpText;
    public String api;
    public boolean messageHandler;
    public boolean dependency;
    public boolean hidden;
    public boolean autoInstall;

    public List<String> commands;
    public List<String> permissions;
    public List<String> dependencies;
    private TwasiPlugin twasiPlugin;

    public static PluginConfig fromInputStream(InputStream stream) throws Exception {
        // Parse
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        return mapper.readValue(stream, PluginConfig.class);
    }

    public String getName() {
        return name;
    }

    public String getLocalizedName(User user) {
        return this.twasiPlugin.getLocalizedName(user);
    }

    public String getDescription() {
        return description;
    }

    public String getLocalizedDescription(User user) {
        return this.twasiPlugin.getLocalizedDescription(user);
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

    public List<String> getPermissions() {
        if (permissions == null) {
            permissions = new ArrayList<>();
        }
        return permissions;
    }

    public boolean handlesMessages() {
        return messageHandler;
    }

    public boolean isDependency() {
        return dependency;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isAutoInstall() {
        return autoInstall;
    }

    public List<String> getCommands() {
        if (commands == null) {
            commands = new ArrayList<>();
        }
        return commands;
    }

    public String getHelpText() {
        return helpText;
    }

    public String getApi() {
        return api;
    }

    public List<String> getDependencies() {
        if (dependencies == null) {
            dependencies = new ArrayList<>();
        }
        return dependencies;
    }

    void setLocalizationPluginClass(TwasiPlugin twasiPlugin) {
        this.twasiPlugin = twasiPlugin;
    }
}
