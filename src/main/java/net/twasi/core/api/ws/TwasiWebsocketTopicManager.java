package net.twasi.core.api.ws;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.twasi.core.api.ws.models.TwasiWebsocketAnswer;
import net.twasi.core.api.ws.models.TwasiWebsocketClient;
import net.twasi.core.api.ws.models.TwasiWebsocketMessage;

import java.util.HashMap;
import java.util.Map;

public class TwasiWebsocketTopicManager {

    private Map<String, TwasiWebsocketEndpoint<WebsocketClientConfig>> topicEndpoints = new HashMap<>();

    public void addTopic(TwasiWebsocketEndpoint<WebsocketClientConfig> endpoint) {
        String topicName = endpoint.getTopic();
        if (endpoint.getProvidingPlugin() != null)
            topicName = endpoint.getProvidingPlugin().getName() + '/' + topicName;

        if (topicEndpoints.containsKey(topicName))
            throw new RuntimeException("A topic with that name (" + topicName + ") is already registered.");

        topicEndpoints.put(topicName, endpoint);
    }

    public JsonElement handle(TwasiWebsocketClient ws, JsonObject msg) {
        String topic = msg.get("topic").getAsString();

        if (!topicEndpoints.containsKey(topic))
            throw new WebsocketHandledException("There is no topic with that name (" + topic + ").");

        TwasiWebsocketEndpoint<WebsocketClientConfig> endpoint = topicEndpoints.get(topic);

        String scope = msg.get("scope").getAsString();
        if (scope.equalsIgnoreCase("action")) {

            if (ws.getAuthentication() == null && !endpoint.allowAnonymousAction())
                throw new WebsocketHandledException("You have to be authenticated to execute any actions on this topic (" + topic + ")");

            JsonElement action = msg.get("action");
            return endpoint.handle(new TwasiWebsocketMessage(action, msg, ws));

        } else if (scope.equalsIgnoreCase("subscribe")) {

            if (!(endpoint instanceof TwasiWebsocketListenerEndpoint))
                throw new WebsocketHandledException("You tried to subscribe to a topic that is unsubscribable (" + topic + ").");

            TwasiWebsocketListenerEndpoint<?> lEndpoint = (TwasiWebsocketListenerEndpoint) endpoint;
            if (ws.getAuthentication() == null && !lEndpoint.allowAnonymousListening())
                throw new WebsocketHandledException("You have to be authenticated to subscribe to this topic (" + topic + ").");

            Gson gson = new Gson();
            WebsocketClientConfig config = gson.fromJson(msg.get("config"), lEndpoint.getConfigClass());

            try {
                lEndpoint.addListener(ws, config);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new WebsocketHandledException("Invalid config provided");
            }
            return TwasiWebsocketAnswer.success();
        }

        throw new WebsocketHandledException("No message scope provided.");
    }

}
