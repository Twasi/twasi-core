package net.twasi.core.services.providers.connector;

public class IrcConnectionInfo {
    private String twitchId;
    private String twitchName;

    private boolean hasCustomBot;
    private String botName;
    private String botAuth;

    public IrcConnectionInfo(String twitchId, String twitchName) {
        this.twitchId = twitchId;
        this.twitchName = twitchName;
        this.hasCustomBot = false;
    }

    public IrcConnectionInfo(String twitchId, String twitchName, String botName, String botAuth) {
        this.twitchId = twitchId;
        this.twitchName = twitchName;
        this.hasCustomBot = true;
        this.botName = botName;
        this.botAuth = botAuth;
    }
}
