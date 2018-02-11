package net.twasi.core.plugin.api.events;

import net.twasi.core.database.models.permissions.Permissions;
import net.twasi.core.plugin.api.TwasiUserPlugin;

public class TwasiInstallEvent extends TwasiEvent {
    private Permissions defaultGroup;
    private Permissions moderatorsGroup;
    private Permissions adminGroup;

    public TwasiInstallEvent(TwasiUserPlugin userPlugin, Permissions defaultGroup, Permissions moderatorsGroup, Permissions adminGroup) {
        super(userPlugin);
        this.defaultGroup = defaultGroup;
        this.moderatorsGroup = moderatorsGroup;
        this.adminGroup = adminGroup;
    }

    public Permissions getDefaultGroup() {
        return defaultGroup;
    }

    public Permissions getModeratorsGroup() {
        return moderatorsGroup;
    }

    public Permissions getAdminGroup() {
        return adminGroup;
    }
}
