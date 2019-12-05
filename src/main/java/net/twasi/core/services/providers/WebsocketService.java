package net.twasi.core.services.providers;

import net.twasi.core.api.ws.TwasiWebsocketServlet;
import net.twasi.core.api.ws.api.TwasiWebsocketEndpoint;
import net.twasi.core.api.ws.providers.AuthenticationEndpoint;
import net.twasi.core.services.IService;
import net.twasi.core.services.ServiceRegistry;

public class WebsocketService implements IService {

    public static WebsocketService get() {
        return ServiceRegistry.get(WebsocketService.class);
    }

    public WebsocketService() {
        registerDefaultWebsocketEndpoints();
    }

    public void addEndpoint(TwasiWebsocketEndpoint<?> endpoint) {
        TwasiWebsocketServlet.topicManager.addTopic(endpoint); // TODO Solve this better than using "public static"
    }

    private void registerDefaultWebsocketEndpoints() {
        addEndpoint(new AuthenticationEndpoint());
    }

}