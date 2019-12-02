package net.twasi.core.api.ws;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Calendar;

public class TwasiWebsocketEvent<T> {

    public final String event;
    public final T details;
    public final long timeStamp;

    public TwasiWebsocketEvent(T t, String eventName) {
        this.details = t;
        this.timeStamp = Calendar.getInstance().getTime().getTime();
        this.event = eventName;
    }

    public JsonElement toSendable() {
        return new Gson().toJsonTree(this);
    }

}
