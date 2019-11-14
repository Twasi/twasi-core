package net.twasi.core.api.ws;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TwasiWebsocketMessage {

    private JsonElement action;
    private JsonObject originalMessage;
    private TwasiWebsocketClient client;

    public TwasiWebsocketMessage(JsonElement action, JsonObject originalMessage, TwasiWebsocketClient client) {
        this.action = action;
        this.originalMessage = originalMessage;
        this.client = client;
    }

    public JsonElement getAction() {
        return action;
    }

    public JsonObject getOriginalMessage() {
        return originalMessage;
    }

    public TwasiWebsocketClient getClient() {
        return client;
    }
}
