package net.twasi.core.graphql.model;

import java.util.List;

public class UserDTO {

    private final String id;
    private final TwitchAccountDTO twitchAccount;
    private final List<String> installedPlugins;
    private final List<EventMessageDTO> eventMessages;

    public UserDTO(String id, TwitchAccountDTO twitchAccount, List<String> installedPlugins, List<EventMessageDTO> eventMessages) {
        this.id = id;
        this.twitchAccount = twitchAccount;
        this.installedPlugins = installedPlugins;
        this.eventMessages = eventMessages;
    }

    public String getId() {
        return this.id;
    }

    public TwitchAccountDTO getTwitchAccount() {
        return twitchAccount;
    }

    public List<String> getInstalledPlugins() {
        return installedPlugins;
    }

    public List<EventMessageDTO> getEvents() {
        return eventMessages;
    }
}
