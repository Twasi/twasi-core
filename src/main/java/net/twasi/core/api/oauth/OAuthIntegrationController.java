package net.twasi.core.api.oauth;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.twasi.core.database.models.User;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.IService;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.JWTService;
import net.twasi.core.services.providers.config.ConfigService;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OAuthIntegrationController extends HttpServlet implements IService {

    private BiMap<String, OAuthState> states = HashBiMap.create();
    private List<IOauthIntegrationHandler> handlers = new ArrayList<>();

    public OAuthIntegrationController() {
        ServiceRegistry.register(this);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String[] parts = req.getPathInfo().substring(1).split("/"); // Substring to remove leading slash
            String oauthService = parts[0];
            IOauthIntegrationHandler handler = this.handlers.stream().filter(h -> h.getOauthServiceName().equalsIgnoreCase(oauthService)).findFirst().orElse(null);

            boolean callback = parts.length > 1;
            if (callback) callback = parts[1].equals("callback");

            Map<String, String[]> params = req.getParameterMap();
            if (params == null) params = new HashMap<>();


            if (params.keySet().contains(handler.getStateParameterName()) && callback) {
                OAuthState state = states.get(req.getParameter(handler.getStateParameterName()));
                User user = state.user;
                states.remove(req.getParameter(handler.getStateParameterName()));
                handler.handleResponse(req.getParameterMap(), user);
                resp.sendRedirect(state.environment);
                return;
            }

            User user = JWTService.get().getManager().getUserFromToken(req.getParameter("jwt"));
            if (states.values().stream().anyMatch(state -> state.user == user))
                states.remove(states.inverse().get(states.values().stream().filter(state -> state.user == user).findFirst().get()));
            String state;
            do state = RandomStringUtils.randomAlphanumeric(20);
            while (states.containsKey(state));

            resp.sendRedirect(handler.getOauthUri(state));

            String redirectUri = req.getParameter("environment");
            String finalRedirectUri = redirectUri;
            if (redirectUri == null || ConfigService.get().getCatalog().auth.endpoints.stream().noneMatch(ep -> ep.equalsIgnoreCase(finalRedirectUri)))
                redirectUri = "https://panel.twasi.net";

            this.states.put(state, new OAuthState(user, redirectUri));
        } catch (Exception e) {
            TwasiLogger.log.warn("OAuth request could not be handled");
            TwasiLogger.log.debug("OAuth request failed", e);
            resp.sendRedirect(ConfigService.get().getCatalog().webinterface.self);
        }
    }

    public void registerOauthIntegrationHandler(IOauthIntegrationHandler handler) {
        this.handlers.add(handler);
        TwasiLogger.log.info("OAuth provider registered: " + handler.getOauthServiceName());
    }
}
