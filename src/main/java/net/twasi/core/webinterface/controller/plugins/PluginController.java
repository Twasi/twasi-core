package net.twasi.core.webinterface.controller.plugins;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.plugin.api.TwasiPlugin;
import net.twasi.core.services.PluginManagerService;
import net.twasi.core.webinterface.dto.ApiDTO;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

import java.util.List;
import java.util.stream.Collectors;

public class PluginController extends RequestHandler {

    @Override
    public void handleGet(HttpExchange t) {
        List<TwasiPlugin> plugins = PluginManagerService.getService().getPlugins();

        PluginListDTO dto = new PluginListDTO(plugins);

        Commons.writeDTO(t, dto, 200);
    }

    class PluginListDTO extends ApiDTO {
        List<SinglePluginDTO> plugins;

        PluginListDTO(List<TwasiPlugin> plugins) {
            super(true);
            this.plugins = plugins.stream().map(SinglePluginDTO::new).collect(Collectors.toList());
        }

        class SinglePluginDTO {
            String name;
            String author;
            String description;
            String version;

            SinglePluginDTO(TwasiPlugin plugin) {
                name = plugin.getConfig().name;
                author = plugin.getConfig().author;
                description = plugin.getConfig().description;
                version = plugin.getConfig().version;
            }
        }
    }

}
