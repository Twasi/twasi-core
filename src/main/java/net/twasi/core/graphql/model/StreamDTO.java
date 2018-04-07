package net.twasi.core.graphql.model;

public class StreamDTO {
    private final String id;
    private final String streamId;

    private final DurationDTO duration;
    private final boolean isLive;

    private final int followerPlus;
    private final int clicksPlus;
    private final int viewersMax;
    private final int viewersAverage;

    public StreamDTO(String id, String streamId, DurationDTO duraton, boolean isLive, int followerPlus, int clicksPlus, int viewersMax, int viewersAverage) {
        this.id = id;
        this.streamId = streamId;

        this.duration = duraton;
        this.isLive = isLive;

        this.followerPlus = followerPlus;
        this.clicksPlus = clicksPlus;
        this.viewersMax = viewersMax;
        this.viewersAverage = viewersAverage;
    }

    public String getId() {
        return id;
    }

    public String getStreamId() {
        return streamId;
    }

    public DurationDTO getDuration() {
        return duration;
    }

    public boolean isLive() {
        return isLive;
    }

    public int getFollowerPlus() {
        return followerPlus;
    }

    public int getClicksPlus() {
        return clicksPlus;
    }

    public int getViewersMax() {
        return viewersMax;
    }

    public int getViewersAverage() {
        return viewersAverage;
    }
}
