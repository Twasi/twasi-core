package net.twasi.core.graphql.model;

import net.twasi.core.database.models.User;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.PluginManagerService;

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
                .map(p -> new PluginDetailsDTO(p.getDescription(), user.getInstalledPlugins().contains(p.getName())))
                .collect(Collectors.toList());
    }
}
