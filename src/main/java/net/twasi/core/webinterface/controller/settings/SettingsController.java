package net.twasi.core.webinterface.controller.settings;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.database.models.GlobalConfig;
import net.twasi.core.database.models.User;
import net.twasi.core.webinterface.dto.ApiDTO;
import net.twasi.core.webinterface.dto.error.UnauthorizedDTO;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

public class SettingsController extends RequestHandler {

    @Override
    public void handleGet(HttpExchange t) {
        if (!isAuthenticated(t)) {
            Commons.writeDTO(t, new UnauthorizedDTO(), 401);
        }
        User user = getUser(t);

        GlobalConfig config = user.getConfig();
        SettingsDTO dto = new SettingsDTO(config);

        Commons.writeDTO(t, dto, 200);
    }

    class SettingsDTO extends ApiDTO {
        String language;

        SettingsDTO(GlobalConfig config) {
            super(true);
            language = config.getLanguage();
        }
    }
}
