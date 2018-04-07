package net.twasi.core.graphql.model;

public class AllTimeStatsDTO {
    private final int viewersMax;
    private final int totalStreamDuration;
    private final int hoursViewed;

    // Optional
    private int totalTokens;
    private int totalSongrequestsPlayed;
    private int totalQuotes;

    public AllTimeStatsDTO(int viewersMax, int totalStreamDuration, int hoursViewed) {
        this.viewersMax = viewersMax;
        this.totalStreamDuration = totalStreamDuration;
        this.hoursViewed = hoursViewed;
    }

    public int getViewersMax() {
        return viewersMax;
    }

    public int getTotalStreamDuration() {
        return totalStreamDuration;
    }

    public int getHoursViewed() {
        return hoursViewed;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }

    public int getTotalSongrequestsPlayed() {
        return totalSongrequestsPlayed;
    }

    public void setTotalSongrequestsPlayed(int totalSongrequestsPlayed) {
        this.totalSongrequestsPlayed = totalSongrequestsPlayed;
    }

    public int getTotalQuotes() {
        return totalQuotes;
    }

    public void setTotalQuotes(int totalQuotes) {
        this.totalQuotes = totalQuotes;
    }
}
