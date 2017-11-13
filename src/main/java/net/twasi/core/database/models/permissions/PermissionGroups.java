package net.twasi.core.database.models.permissions;

public enum PermissionGroups {

    // Only the broadcaster
    BROADCASTER,

    // Only moderators (inherits broadcaster)
    MODERATOR,

    // Subscriber (inherits moderator)
    SUBSCRIBERS,

    // Viewer (all)
    VIEWER

}
