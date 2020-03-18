package net.twasi.core.graphql.model;

import net.twasi.core.database.models.Language;
import net.twasi.core.database.models.User;
import net.twasi.core.database.models.UserRank;
import net.twasi.core.graphql.model.customthemes.CustomThemesDTO;
import net.twasi.core.graphql.model.support.SupportDTO;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.services.providers.PluginManagerService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PanelDTO {
    private User user;
    private AppInfoDTO appInfo = new AppInfoDTO();
    PluginManagerService pm = PluginManagerService.get();

    public PanelDTO(User user) {
        this.user = user;
    }

    public UserDTO getUser() {
        return new UserDTO(user);
    }

    public BotStatusDTO getStatus() {
        return new BotStatusDTO(user);
    }

    public UserStatusDTO getUserStatus() {
        return new UserStatusDTO(user);
    }

    public AppInfoDTO getAppInfo() {
        return appInfo;
    }

    private Stream<TwasiPlugin> getPlStream() {
        return pm.getPlugins().stream()
                .filter(p -> !p.getDescription().isHidden());
    }

    public List<PluginDetailsDTO> getInstalledPlugins() {
        return getPlStream()
                .filter(pl -> user.getInstalledPlugins().contains(pl.getDescription().name))
                .map(pl -> new PluginDetailsDTO(pl, user, true))
                .collect(Collectors.toList());
    }

    public GraphQLPagination<PluginDetailsDTO> getAvailablePlugins() {
        return new GraphQLPagination<>(
                getPlStream()::count,
                (pg) -> GraphQLPagination.paginateStream(getPlStream(), pg)
                        .map(p -> new PluginDetailsDTO(p, user, user.getInstalledPlugins().contains(p.getName())))
                        .collect(Collectors.toList()));
    }

    public SupportDTO getSupport() {
        return new SupportDTO(user);
    }

    public AdminDTO getAdmin() {
        // Is the user a team member
        if (user.getRank() == UserRank.TEAM) {
            return new AdminDTO(user);
        }
        return null;
    }

    public List<String> getAvailableLanguages() {
        return Arrays.stream(Language.values()).map(Enum::toString).collect(Collectors.toList());
    }

    public CustomThemesDTO getThemes() {
        return new CustomThemesDTO(user);
    }
}
