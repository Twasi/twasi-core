package net.twasi.core.api.ws.api;

import com.google.gson.JsonElement;
import net.twasi.core.api.ws.models.TwasiWebsocketMessage;
import net.twasi.core.plugin.TwasiPlugin;

import java.lang.reflect.ParameterizedType;

public abstract class TwasiWebsocketEndpoint<T extends WebsocketClientConfig> {

    public final Class<T> getConfigClass() {
        String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
        try {
            return (Class<T>) getClass().getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public abstract String getTopic();

    public boolean allowAnonymousAction() {
        return false;
    }

    private TwasiPlugin<?> providingPlugin = null;

    public TwasiPlugin<?> getProvidingPlugin() {
        return providingPlugin;
    }

    public void setProvidingPlugin(TwasiPlugin<?> providingPlugin) {
        this.providingPlugin = providingPlugin;
    }

    public JsonElement handle(TwasiWebsocketMessage msg) throws Exception {
        return null;
    }

}
