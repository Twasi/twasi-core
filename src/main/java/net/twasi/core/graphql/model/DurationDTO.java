package net.twasi.core.graphql.model;

public class DurationDTO {
    private final int from;
    private final int to;

    public DurationDTO(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getDuration() {
        return getTo() - getFrom();
    }
}
