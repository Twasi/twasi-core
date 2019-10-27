package net.twasi.core.graphql.model.customthemes;

import net.twasi.core.database.models.CustomTheme;
import net.twasi.core.database.models.User;

public class StoreCustomThemeDTO {

    private CustomTheme theme;
    private User queryingUser;

    public StoreCustomThemeDTO(CustomTheme theme, User queryingUser) {
        this.theme = theme;
        this.queryingUser = queryingUser;
    }

    public String getId() {
        return theme.getId().toString();
    }

    public String getName() {
        return theme.getName();
    }

    public String getCreator() {
        return theme.getCreator().getTwitchAccount().getDisplayName();
    }

    public String getCreated() {
        return theme.getCreated().toString();
    }

    public boolean isInstalled() {
        return queryingUser.getInstalledThemes().contains(getId());
    }

    public boolean isApproved() {
        return theme.isApproved();
    }

    public CustomThemeDTO getTheme() {
        return theme.getProperties();
    }

}
