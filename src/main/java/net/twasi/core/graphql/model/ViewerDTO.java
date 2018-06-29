package net.twasi.core.graphql.model;

import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.PluginManagerService;

import java.util.List;
import java.util.stream.Collectors;

public class ViewerDTO {
    private UserDTO user;
    private BotStatusDTO status;
    private UserStatusDTO userStatus;
    private AppInfoDTO appInfo = new AppInfoDTO();

    public ViewerDTO(UserDTO user, BotStatusDTO status, UserStatusDTO userStatus) {
        this.user = user;
        this.status = status;
        this.userStatus = userStatus;
    }

    public UserDTO getUser() {
        return user;
    }

    public BotStatusDTO getStatus() {
        return status;
    }

    public UserStatusDTO getUserStatus() {
        return userStatus;
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
