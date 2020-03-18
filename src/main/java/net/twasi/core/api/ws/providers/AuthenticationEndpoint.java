package net.twasi.core.api.ws.providers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.twasi.core.api.ws.api.TwasiWebsocketEndpoint;
import net.twasi.core.api.ws.api.WebsocketClientConfig;
import net.twasi.core.api.ws.models.TwasiWebsocketAnswer;
import net.twasi.core.api.ws.models.TwasiWebsocketAuthentication;
import net.twasi.core.api.ws.models.TwasiWebsocketAuthentication.AuthenticationType;
import net.twasi.core.api.ws.models.TwasiWebsocketMessage;
import net.twasi.core.api.ws.models.WebsocketHandledException;
import net.twasi.core.database.models.TwitchAccount;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AuthenticationEndpoint extends TwasiWebsocketEndpoint<WebsocketClientConfig> {

    @Override
    public String getTopic() {
        return "auth";
    }

    @Override
    public JsonElement handle(TwasiWebsocketMessage msg) {
        AuthenticationType type;
        String token;
        try {
            JsonObject a = msg.getAction().getAsJsonObject();
            type = AuthenticationType.valueOf(a.get("type").getAsString());
            token = a.get("token").getAsString();
        } catch (Exception e) {
            String types = Arrays.stream(AuthenticationType.values()).map(Enum::toString).collect(Collectors.joining(", "));
            throw new WebsocketHandledException("Invalid auth request. Available AuthenticationTypes: " + types + ". Please provide 'type' and 'token' fields.");
        }
        if (msg.getClient().getAuthentication() != null)
            throw new WebsocketHandledException("Already authenticated. Please just authenticate once per client.");
        TwasiWebsocketAuthentication auth = new TwasiWebsocketAuthentication(type, token);
        msg.getClient().setAuthentication(auth);

        JsonObject result = new JsonObject(), user = new JsonObject();
        TwitchAccount acc = auth.getUser().getTwitchAccount();
        user.addProperty("twitchId", acc.getTwitchId());
        user.addProperty("userName", acc.getUserName());
        user.addProperty("displayName", acc.getDisplayName());
        result.add("user", user);

        return TwasiWebsocketAnswer.success(result);
    }

    @Override
    public boolean allowAnonymousAction() {
        return true;
    }
}
