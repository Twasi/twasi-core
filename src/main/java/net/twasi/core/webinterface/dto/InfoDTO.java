package net.twasi.core.webinterface.dto;

public class InfoDTO extends ApiDTO {
    private String twitchid;
    private String username;
    private boolean isRunning;


    public InfoDTO(boolean status, String twitchid, String username, boolean isInstanceRunning) {
        super(status);
        this.twitchid = twitchid;
        this.username = username;
        this.isRunning = isInstanceRunning;
    }
}
