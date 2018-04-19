package net.twasi.core.graphql.model;

public class CurrentUserStatsDTO {
    private final int follower;
    private final int viewerTracked;
    private final int chatMessages;
    private final int streams;

    public CurrentUserStatsDTO(int follower, int viewerTracked, int chatMessages, int streams) {
        this.follower = follower;
        this.viewerTracked = viewerTracked;
        this.chatMessages = chatMessages;
        this.streams = streams;
    }

    public int getFollower() {
        return follower;
    }

    public int getViewerTracked() {
        return viewerTracked;
    }

    public int getChatMessages() {
        return chatMessages;
    }

    public int getStreams() {
        return streams;
    }
}
