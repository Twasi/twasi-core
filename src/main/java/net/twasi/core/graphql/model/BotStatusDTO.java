package net.twasi.core.graphql.model;

public class BotStatusDTO {
    private boolean isRunning;

    public BotStatusDTO(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public BotStatusDTO changeStatus(Boolean isRunning) {
        System.out.println(isRunning);
        return new BotStatusDTO(isRunning);
    }
}
