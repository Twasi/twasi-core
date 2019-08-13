package net.twasi.core.graphql.model;

import net.twasi.core.database.models.User;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.PluginConfig;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.services.providers.InstanceManagerService;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

public class PluginDetailsDTO {
    private final TwasiPlugin plugin;

    private final String name;
    private final String description;

    private final boolean isInstalled;

    public PluginDetailsDTO(TwasiPlugin plugin, User user, boolean isInstalled) {
        this.plugin = plugin;
        if (user != null) {
            name = plugin.getDescription().getLocalizedName(user);
            description = plugin.getDescription().getLocalizedDescription(user);
        } else {
            name = plugin.getDescription().getName();
            description = plugin.getDescription().getDescription();
        }

        this.isInstalled = isInstalled;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return plugin.getDescription().getAuthor();
    }

    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public List<String> getCommands() {
        return plugin.getDescription().getCommands();
    }

    public List<String> getPermissions() {
        return plugin.getDescription().getPermissions();
    }

    public String getId() {
        return plugin.getDescription().getName();
    }

    public String getBanner() {
        InputStream bannerInputStream = plugin.getClassLoader().getResourceAsStream("banner.png");

        if (bannerInputStream == null) {
            return null;
        }

        try {
            byte[] encoded = Base64.getEncoder().encode(IOUtils.toByteArray(bannerInputStream));

            return "data:image/png," + new String(encoded);
        } catch (IOException e) {
            TwasiLogger.log.debug("Failed to resolve banner.", e);
            return null;
        }
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public long getInstallations() {
        return InstanceManagerService.get()
                .getInterfaces().stream()
                .filter(i -> i.getPlugins()
                        .stream().anyMatch(pl -> pl.getCorePlugin()
                                .getDescription().name.equalsIgnoreCase(this.getId()))).count();
    }
}
