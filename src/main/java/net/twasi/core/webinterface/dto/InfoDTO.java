package net.twasi.core.webinterface.dto;

public class InfoDTO extends ApiDTO {
    public String twitchid;
    public String username;
    public boolean isRunning;


    public InfoDTO(boolean status, String twitchid, String username, boolean isInstanceRunning) {
        super(status);
        this.twitchid = twitchid;
        this.username = username;
        this.isRunning = isInstanceRunning;
    }
}
