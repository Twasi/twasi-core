package net.twasi.core.webinterface.controller.plugins;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.database.Database;
import net.twasi.core.database.models.User;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.services.InstanceManagerService;
import net.twasi.core.services.PluginManagerService;
import net.twasi.core.webinterface.dto.ApiDTO;
import net.twasi.core.webinterface.dto.SuccessDTO;
import net.twasi.core.webinterface.dto.error.BadRequestDTO;
import net.twasi.core.webinterface.dto.error.UnauthorizedDTO;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PluginController extends RequestHandler {

    int size = 4;

    @Override
    public void handleGet(HttpExchange t) {
        if (!isAuthenticated(t)) {
            Commons.writeDTO(t, new UnauthorizedDTO(), 401);
            return;
        }

        Map<String, String> params = Commons.parseQueryParams(t);

        int page = Integer.valueOf((params.getOrDefault("page", "0")));
        String q = params.getOrDefault("q", "");

        List<TwasiPlugin> plugins = PluginManagerService.getService().getPlugins();

        if (!q.isEmpty()) {
            plugins = plugins.stream().filter(plugin ->
                    plugin.getDescription().getName().contains(q) ||
                            plugin.getDescription().getAuthor().contains(q) ||
                            plugin.getDescription().getDescription().contains(q)
            ).collect(Collectors.toList());
        }

        if (plugins.size() > size) {
            plugins = plugins.subList(page * size, (page + 1) * size);
        }

        PluginListDTO dto = new PluginListDTO(plugins, getUser(t));

        Commons.writeDTO(t, dto, 200);
    }

    @Override
    public void handlePost(HttpExchange t) {
        if (!isAuthenticated(t)) {
            Commons.writeDTO(t, new UnauthorizedDTO(), 401);
            return;
        }
        User user = getUser(t);

        PluginInstallDTO dto = new Gson().fromJson(new InputStreamReader(t.getRequestBody()), PluginInstallDTO.class);

        if (dto == null || dto.action == null) {
            Commons.writeDTO(t, new BadRequestDTO("Invalid data submitted"), 400);
            return;
        }

        if (dto.action == PluginInstallDTO.ActionType.INSTALL) {
            // Installation
            if (user.getInstalledPlugins().contains(dto.pluginName)) {
                Commons.writeDTO(t, new BadRequestDTO("Plugin is already installed."), 400);
                return;
            }

            if (PluginManagerService.getService().getPlugins().stream().noneMatch(twasiPlugin -> twasiPlugin.getName().equalsIgnoreCase(dto.pluginName))) {
                Commons.writeDTO(t, new BadRequestDTO("This plugin is not available on this instance."), 400);
                return;
            }

            TwasiInterface inf = InstanceManagerService.getService().getByUser(user);
            inf.installPlugin(PluginManagerService
                    .getService()
                    .getPlugins()
                    .stream()
                    .filter(twasiPlugin -> twasiPlugin
                            .getName()
                            .equalsIgnoreCase(dto.pluginName)
                    )
                    .findFirst().get()
            );

            user.getInstalledPlugins().add(dto.pluginName);
            Database.getStore().save(user);

            Commons.writeDTO(t, new SuccessDTO("Plugin installed"), 200);
        } else if (dto.action == PluginInstallDTO.ActionType.UNINSTALL) {
            // Uninstallation
            if (!user.getInstalledPlugins().contains(dto.pluginName)) {
                Commons.writeDTO(t, new BadRequestDTO("Plugin is not installed."), 400);
                return;
            }

            if (PluginManagerService.getService().getPlugins().stream().noneMatch(twasiPlugin -> twasiPlugin.getName().equalsIgnoreCase(dto.pluginName))) {
                Commons.writeDTO(t, new BadRequestDTO("This plugin is not available on this instance."), 400);
                return;
            }

            TwasiInterface inf = InstanceManagerService.getService().getByUser(user);
            inf.uninstallPlugin(PluginManagerService.getService().getPlugins().stream().filter(twasiPlugin -> twasiPlugin.getName().equalsIgnoreCase(dto.pluginName)).findFirst().get());

            user.getInstalledPlugins().remove(dto.pluginName);
            Database.getStore().save(user);

            Commons.writeDTO(t, new SuccessDTO("Plugin uninstalled"), 200);
        } else {
            Commons.writeDTO(t, new BadRequestDTO("Unknown action"), 400);
        }
    }

    static class PluginInstallDTO {
        String pluginName;
        ActionType action;

        enum ActionType {
            INSTALL,
            UNINSTALL
        }
    }

    class PluginListDTO extends ApiDTO {
        List<SinglePluginDTO> plugins;

        PluginListDTO(List<TwasiPlugin> plugins, User user) {
            super(true);

            this.plugins = plugins.stream().map(plugin -> new SinglePluginDTO(plugin, user.getInstalledPlugins().contains(plugin.getName()))).collect(Collectors.toList());
        }

        class SinglePluginDTO {
            String name;
            String author;
            String description;
            String version;
            boolean isInstalled;

            SinglePluginDTO(TwasiPlugin plugin, Boolean isInstalled) {
                name = plugin.getDescription().name;
                author = plugin.getDescription().author;
                description = plugin.getDescription().description;
                version = plugin.getDescription().version;
                this.isInstalled = isInstalled;
            }
        }
    }

}
