package net.twasi.core.webinterface.dto;

public class TwitchAccountDTO {

    public String name;
    public String token;

    public boolean isValid() {
        return name != null && token != null;
    }

}
