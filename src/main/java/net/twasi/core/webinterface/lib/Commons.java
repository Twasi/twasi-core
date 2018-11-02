package net.twasi.core.webinterface.lib;

import com.google.gson.Gson;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.webinterface.dto.ApiDTO;
import net.twasi.core.webinterface.dto.error.UnallowedMethodDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Commons {

    public static void handleUnallowedMethod(HttpServletResponse t) {
        writeDTO(t, new UnallowedMethodDTO(), 405);
    }

    public static void writeString(HttpServletResponse t, String s, int code) {
        try {
            t.setStatus(code);
            t.getWriter().println(s);
            t.getWriter().close();
        } catch (IOException e) {
            TwasiLogger.log.error(e);
        }
    }

    public static void writeDTO(HttpServletResponse t, ApiDTO dto, int code) {
        String json = new Gson().toJson(dto);
        t.setHeader("Access-Control-Allow-Origin", "*");
        t.setHeader("Content-Type", "application/json");
        writeString(t, json, code);
    }
}
