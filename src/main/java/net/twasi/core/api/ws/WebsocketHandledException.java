package net.twasi.core.api.ws;

public class WebsocketHandledException extends RuntimeException {

    private boolean log = true;

    public WebsocketHandledException(String s) {
        super(s);
    }

    public WebsocketHandledException(String s, boolean log) {
        super(s);
        this.log = log;
    }

    public boolean shouldLog() {
        return log;
    }
}
