package net.twasi.core.graphql.model;

import net.twasi.core.database.models.User;
import net.twasi.core.database.models.UserRank;

import java.util.Collections;
import java.util.List;

public class UserDTO {

    private final String id;
    private final TwitchAccountDTO twitchAccount;
    private final List<String> installedPlugins;
    private final List<EventMessageDTO> eventMessages;

    private final StreamDTO latestStream;
    private final List<StreamDTO> streams;

    private final CurrentUserStatsDTO currentStats;
    private final AllTimeStatsDTO alltimeStats;

    private final UserRank rank;

    public UserDTO(String id, TwitchAccountDTO twitchAccount, List<String> installedPlugins, List<EventMessageDTO> eventMessages, StreamDTO latestStream, List<StreamDTO> streams, CurrentUserStatsDTO currentStats, AllTimeStatsDTO alltimeStats, UserRank rank) {
        this.id = id;
        this.twitchAccount = twitchAccount;
        this.installedPlugins = installedPlugins;
        this.eventMessages = eventMessages;

        this.latestStream = latestStream;
        this.streams = streams;

        this.currentStats = currentStats;
        this.alltimeStats = alltimeStats;

        this.rank = rank;
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

    public static UserDTO fromUser(User user) {
        StreamDTO stream = new StreamDTO("TwasiID", "TwitchID", new DurationDTO(100, 200), false, 0, 0, 0, 0);

        return new UserDTO(
                user.getId().toString(),
                new TwitchAccountDTO(user.getTwitchAccount()),
                user.getInstalledPlugins(),
                EventMessageDTO.fromEvents(user.getEvents()),
                stream,
                Collections.singletonList(stream),
                new CurrentUserStatsDTO(0, 0, 0, 0),
                new AllTimeStatsDTO(0, 0, 0),
                UserRank.STREAMER
        );
    }

    public StreamDTO getLatestStream() {
        return latestStream;
    }

    public List<StreamDTO> getStreams() {
        return streams;
    }

    public CurrentUserStatsDTO getCurrentStats() {
        return currentStats;
    }

    public AllTimeStatsDTO getAlltimeStats() {
        return alltimeStats;
    }

    public UserRank getRank() {
        return rank;
    }
}
