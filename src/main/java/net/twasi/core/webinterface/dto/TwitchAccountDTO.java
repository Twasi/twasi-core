package net.twasi.core.webinterface.dto;

class TwitchAccountDTO {

    private String name;
    private String token;

    boolean isValid() {
        return name != null && token != null;
    }

}
