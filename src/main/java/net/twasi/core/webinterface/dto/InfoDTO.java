package net.twasi.core.webinterface.dto;

public class InfoDTO extends ApiDTO {
    public String twitchid;
    public String username;


    public InfoDTO(boolean status, String twitchid, String username) {
        super(status);
        this.twitchid = twitchid;
        this.username = username;
    }
}
