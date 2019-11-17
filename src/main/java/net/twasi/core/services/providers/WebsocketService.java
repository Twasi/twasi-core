package net.twasi.core.services.providers;

import net.twasi.core.api.ws.TwasiWebsocketEndpoint;
import net.twasi.core.api.ws.WebsocketServer;
import net.twasi.core.services.IService;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;

public class WebsocketService implements IService {

    public static WebsocketService get() {
        return ServiceRegistry.get(WebsocketService.class);
    }

    private final WebsocketServer server;

    public WebsocketService() {
        server = new WebsocketServer(ConfigService.get().getCatalog().websocket.port);
    }

    public void addEndpoint(TwasiWebsocketEndpoint endpoint) {
        server.getTopicManager().addTopic(endpoint);
    }

}