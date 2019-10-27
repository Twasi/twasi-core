package net.twasi.core.graphql.model;

import net.twasi.core.database.models.Language;
import net.twasi.core.database.models.User;
import net.twasi.core.database.models.UserRank;
import net.twasi.core.graphql.model.customthemes.CustomThemesDTO;
import net.twasi.core.graphql.model.support.SupportDTO;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.PluginManagerService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PanelDTO {
    private User user;
    private AppInfoDTO appInfo = new AppInfoDTO();

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

    public List<PluginDetailsDTO> getPlugins() {
        return ServiceRegistry.get(PluginManagerService.class)
                .getPlugins()
                .stream()
                .filter(p -> !p.getDescription().isHidden())
                .map(p -> new PluginDetailsDTO(p, user, user.getInstalledPlugins().contains(p.getName())))
                .collect(Collectors.toList());
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
