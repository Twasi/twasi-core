package net.twasi.core.webinterface.controllerold;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.webinterface.dto.ApiDTO;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

public class VersionController extends RequestHandler {

    @Override
    public void handleGet(HttpExchange t) {

        String versionNumber = getClass().getPackage().getImplementationVersion();

        if (versionNumber == null) {
            versionNumber = "LIVE";
        }

        Commons.writeDTO(t, new VersionInfoDTO(versionNumber), 200);
    }

    class VersionInfoDTO extends ApiDTO {
        String version;

        VersionInfoDTO(String version) {
            super(true);

            this.version = version;
        }
    }
}
