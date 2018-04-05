package net.twasi.core.graphql.model;

public class BotStatusDTO {
    private boolean isRunning;

    public BotStatusDTO(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
