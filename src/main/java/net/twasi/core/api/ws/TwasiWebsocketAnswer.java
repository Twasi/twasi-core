package net.twasi.core.api.ws;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class TwasiWebsocketAnswer {

    public static JsonElement success() {
        JsonObject ob = new JsonObject();
        ob.add("status", new JsonPrimitive("success"));
        return ob;
    }

    public static JsonElement success(JsonElement result) {
        JsonObject success = success().getAsJsonObject();
        success.add("result", result);
        return success;
    }

    public static JsonElement warn(String message) {
        JsonObject ob = new JsonObject();
        ob.add("status", new JsonPrimitive("warn"));
        ob.add("message", new JsonPrimitive(message));
        return ob;
    }

}
