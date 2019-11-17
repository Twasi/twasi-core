package net.twasi.core.api.ws;

import com.google.gson.JsonElement;
import net.twasi.core.api.ws.models.TwasiWebsocketClient;

import java.util.ArrayList;
import java.util.List;

public abstract class TwasiWebsocketListenerEndpoint extends TwasiWebsocketEndpoint {

    protected final List<TwasiWebsocketClient> listeners = new ArrayList<>();

    final void addListener(TwasiWebsocketClient client) {
        listeners.add(client);
    }

    final void removeListener(TwasiWebsocketClient client) {
        listeners.remove(client);
    }

    protected final void publish(JsonElement ob) {
        publish(ob.toString());
    }

    protected final void publish(String s) {
        listeners.forEach(l -> l.getConnection().send(s));
    }

    public boolean allowAnonymousListening() {
        return false;
    }

}
