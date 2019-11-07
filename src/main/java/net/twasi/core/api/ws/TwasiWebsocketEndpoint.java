package net.twasi.core.api.ws;

import com.google.gson.JsonElement;
import net.twasi.core.plugin.TwasiPlugin;
import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.List;

public abstract class TwasiWebsocketEndpoint {

    protected final List<WebSocket> listeners = new ArrayList<>();

    private TwasiPlugin providingPlugin = null;

    public TwasiPlugin getProvidingPlugin() {
        return providingPlugin;
    }

    public void setProvidingPlugin(TwasiPlugin providingPlugin) {
        this.providingPlugin = providingPlugin;
    }

    public abstract String getTopic();

    public JsonElement handle(TwasiWebsocketMessage msg) {
        return null;
    }

    final void addListener(WebSocket client) {
        listeners.add(client);
    }

    final void removeListener(WebSocket client) {
        listeners.remove(client);
    }

    protected final void publish(JsonElement ob) {
        publish(ob.toString());
    }

    protected final void publish(String s) {
        listeners.forEach(l -> l.send(s));
    }
}
