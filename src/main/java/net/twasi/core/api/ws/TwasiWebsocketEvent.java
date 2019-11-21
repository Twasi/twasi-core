package net.twasi.core.api.ws;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Calendar;

public class TwasiWebsocketEvent<T> {

    public final T details;
    public final long timeStamp;

    public TwasiWebsocketEvent(T t) {
        this.details = t;
        this.timeStamp = Calendar.getInstance().getTime().getTime();
    }

    public JsonElement toSendable() {
        return new Gson().toJsonTree(this);
    }

}
