package net.twasi.core.models;

import net.twasi.core.database.models.User;

public class Streamer {
    private User user;

    public Streamer(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
