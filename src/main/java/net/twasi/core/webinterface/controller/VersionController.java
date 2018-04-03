package net.twasi.core.webinterface.controller;

import net.twasi.core.webinterface.dto.ApiDTO;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletResponse;

public class VersionController extends RequestHandler {

    @Override
    public void handleGet(Request req, HttpServletResponse res) {

        String versionNumber = getClass().getPackage().getImplementationVersion();

        if (versionNumber == null) {
            versionNumber = "LIVE";
        }

        Commons.writeDTO(res, new VersionInfoDTO(versionNumber), 200);
    }

    class VersionInfoDTO extends ApiDTO {
        String version;

        VersionInfoDTO(String version) {
            super(true);

            this.version = version;
        }
    }
}
