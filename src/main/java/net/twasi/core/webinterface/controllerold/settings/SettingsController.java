package net.twasi.core.webinterface.controllerold.settings;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.database.Database;
import net.twasi.core.database.models.GlobalConfig;
import net.twasi.core.database.models.Language;
import net.twasi.core.database.models.User;
import net.twasi.core.webinterface.dto.ApiDTO;
import net.twasi.core.webinterface.dto.error.BadRequestDTO;
import net.twasi.core.webinterface.dto.error.UnauthorizedDTO;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

import java.io.InputStreamReader;
import java.io.Reader;

public class SettingsController extends RequestHandler {

    @Override
    public void handleGet(HttpExchange t) {
        if (!isAuthenticated(t)) {
            Commons.writeDTO(t, new UnauthorizedDTO(), 401);
        }
        User user = getUser(t);

        GlobalConfig config = user.getConfig();
        SettingsViewDTO dto = new SettingsViewDTO(config);

        Commons.writeDTO(t, dto, 200);
    }

    public void handlePut(HttpExchange t) {
        if (!isAuthenticated(t)) {
            Commons.writeDTO(t, new UnauthorizedDTO(), 401);
        }
        User user = getUser(t);

        Reader reader = new InputStreamReader(t.getRequestBody());
        SettingsInputDTO inputDTO = new Gson().fromJson(reader, SettingsInputDTO.class);

        if (!inputDTO.isValid()) {
            Commons.writeDTO(t, new BadRequestDTO(), 400);
            return;
        }

        GlobalConfig config = inputDTO.toGlobalConfig(user);
        user.setConfig(config);

        Database.getStore().save(user);
        Commons.writeDTO(t, new SettingsPutResponseDTO(), 200);
    }

    class SettingsViewDTO extends ApiDTO {
        String language;

        SettingsViewDTO(GlobalConfig config) {
            super(true);
            language = config.getLanguage().toString();
        }
    }

    class SettingsInputDTO {
        Language language;

        boolean isValid() {
            return language != null;
        }

        GlobalConfig toGlobalConfig(User user) {
            GlobalConfig config = user.getConfig();
            config.setLanguage(language);
            return config;
        }
    }

    class SettingsPutResponseDTO extends ApiDTO {
        public SettingsPutResponseDTO() {
            super(true);
        }
    }
}
