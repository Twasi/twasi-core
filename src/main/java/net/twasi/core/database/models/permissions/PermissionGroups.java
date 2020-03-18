package net.twasi.core.database.models.permissions;

public enum PermissionGroups {

    // Only the broadcaster
    BROADCASTER(40),

    // Only moderators (inherits broadcaster)
    MODERATOR(30),

    // Subscriber (inherits moderator)
    SUBSCRIBERS(10),

    // Viewer (all)
    VIEWER(0);

    private int weight;

    PermissionGroups(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
