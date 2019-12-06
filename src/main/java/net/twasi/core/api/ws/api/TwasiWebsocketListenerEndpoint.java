package net.twasi.core.api.ws.api;

import com.google.gson.JsonElement;
import net.twasi.core.api.ws.models.TwasiWebsocketClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class TwasiWebsocketListenerEndpoint<T extends WebsocketClientConfig> extends TwasiWebsocketEndpoint<T> {

    protected final Map<TwasiWebsocketClient, T> listeners = new HashMap<>();

    public final void addListener(TwasiWebsocketClient client, WebsocketClientConfig config) throws IllegalAccessException, InstantiationException {
        if (config == null) config = getConfigClass().newInstance();
        listeners.put(client, (T) config);
        this.onListenerRegistered(client, (T) config);
    }

    final void removeListener(TwasiWebsocketClient client) {
        listeners.remove(client);
    }

    protected final void publish(JsonElement ob) {
        publish(ob.toString());
    }

    protected final void publish(String s) {
        listeners.keySet().stream().distinct().forEach(l -> {
            try {
                l.send(s);
            } catch (IOException ignored) {
            }
        });
    }

    protected final void publish(Stream<TwasiWebsocketClient> stream, JsonElement ob) {
        publish(stream, ob.toString());
    }

    protected final void publish(Stream<TwasiWebsocketClient> stream, String s) {
        stream.distinct().forEach(l -> {
            try {
                l.send(s);
            } catch (IOException ignored) {
            }
        });
    }

    protected final void publishFilteredByClient(Predicate<TwasiWebsocketClient> filter, JsonElement ob) {
        publishFilteredByClient(filter, ob.toString());
    }

    protected final void publishFilteredByClient(Predicate<TwasiWebsocketClient> filter, String s) {
        publish(listeners.keySet().stream().filter(filter), s);
    }

    protected final void publishFilteredByConfig(Predicate<T> filter, JsonElement ob) {
        publishFilteredByConfig(filter, ob.toString());
    }

    protected final void publishFilteredByConfig(Predicate<T> filter, String s) {
        List<T> correspondingConfigs = listeners.values().stream().filter(filter).collect(Collectors.toList());
        publishFilteredByClient(ws -> correspondingConfigs.contains(listeners.get(ws)), s);
    }

    public boolean allowAnonymousListening() {
        return false;
    }

    protected void onListenerRegistered(TwasiWebsocketClient client, T config) {
    }

}
