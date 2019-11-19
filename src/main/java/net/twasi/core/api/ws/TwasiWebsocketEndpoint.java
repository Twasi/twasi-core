package net.twasi.core.api.ws;

import com.google.gson.JsonElement;
import net.twasi.core.api.ws.models.TwasiWebsocketMessage;
import net.twasi.core.plugin.TwasiPlugin;

public abstract class TwasiWebsocketEndpoint<T extends WebsocketClientConfig> {

    public final Class<? extends WebsocketClientConfig> getConfigClass() {
        return getClass().getTypeParameters()[0].getClass().asSubclass(WebsocketClientConfig.class);
    }

    public abstract String getTopic();

    public boolean allowAnonymousAction() {
        return false;
    }

    private TwasiPlugin providingPlugin = null;

    public TwasiPlugin getProvidingPlugin() {
        return providingPlugin;
    }

    public void setProvidingPlugin(TwasiPlugin providingPlugin) {
        this.providingPlugin = providingPlugin;
    }

    public JsonElement handle(TwasiWebsocketMessage msg) {
        return null;
    }

}
