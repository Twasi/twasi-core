package net.twasi.core.api.oauth;

import net.twasi.core.database.models.User;

import java.util.Map;

public interface IOauthIntegrationHandler {

    String getOauthServiceName();

    String getOauthUri(String context);

    default String getStateParameterName() {
        return null;
    }

    void handleResponse(Map<String, String[]> parameters, User user);

}
