package net.twasi.core.graphql.model.customthemes;

import net.twasi.core.database.models.User;
import net.twasi.core.database.models.UserRank;
import net.twasi.core.database.repositories.CustomThemeRepository;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.graphql.model.GraphQLPagination;
import net.twasi.core.graphql.model.PanelResultDTO;
import net.twasi.core.services.providers.DataService;

import java.util.ArrayList;
import java.util.List;

import static net.twasi.core.graphql.model.PanelResultDTO.PanelResultType.*;

public class CustomThemesDTO {

    private User user;
    private UserRepository userRepo;
    private CustomThemeRepository repo;

    public CustomThemesDTO(User user) {
        this.user = user;
        this.userRepo = DataService.get().get(UserRepository.class);
        this.repo = DataService.get().get(CustomThemeRepository.class);
    }

    public GraphQLPagination<StoreCustomThemeDTO> getMyThemes() {
        return new GraphQLPagination<>(
                () -> repo.countThemesByUser(user),
                (pg) -> repo.getThemesByUser(user, pg)
        );
    }

    public List<StoreCustomThemeDTO> getInstalledThemes() {
        try {
            return repo.getInstalledThemesByUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GraphQLPagination<StoreCustomThemeDTO> getAvailableThemes(boolean approvedOnly) {
        return new GraphQLPagination<>(
                () -> repo.countAvailableThemes(approvedOnly),
                (pg) -> repo.getAvailableThemes(pg, user, approvedOnly)
        );
    }

    public PanelResultDTO uninstall(String themeId) {
        List<String> installedThemes = new ArrayList<>(user.getInstalledThemes());
        installedThemes.remove(themeId);
        user.setInstalledThemes(installedThemes);
        userRepo.commit(user);
        return new PanelResultDTO(OK);
    }

    public PanelResultDTO install(String themeId) {
        List<String> installedThemes = new ArrayList<>(user.getInstalledThemes());
        if (installedThemes.contains(themeId)) {
            return new PanelResultDTO(WARNING, "CUSTOM-THEMES.ALREADY-INSTALLED");
        }
        if (!repo.themeExists(themeId)) {
            return new PanelResultDTO(WARNING, "CUSTOM-THEMES.NOT-EXISTING");
        }
        installedThemes.add(themeId);
        user.setInstalledThemes(installedThemes);
        userRepo.commit(user);
        return new PanelResultDTO(OK);
    }

    public PanelResultDTO delete(String themeId) {
        if (user.getRank() == UserRank.TEAM) {
            if (repo.delete(themeId) > 0) {
                return new PanelResultDTO(OK);
            } else {
                return new PanelResultDTO(WARNING, "CUSTOM-THEMES.NO-THEME-FOUND");
            }
        }
        if (userRepo.getThemeInstallations(themeId) > 0) {
            return new PanelResultDTO(WARNING, "CUSTOM-THEMES.NO-USED-THEME-DELETION");
        } else if (repo.delete(themeId, user) > 0) {
            return new PanelResultDTO(OK);
        } else {
            return new PanelResultDTO(WARNING, "CUSTOM-THEMES.NO-THEME-FOUND");
        }
    }

    public PanelResultDTO create(String name, CustomThemeDTO properties) {
        if (repo.countThemesByUser(user, false) >= 3) {
            return new PanelResultDTO(WARNING, "CUSTOM-THEMES.TOO-MANY-THEMES");
        }
        repo.create(properties, user, name);
        return new PanelResultDTO(OK);
    }

    public PanelResultDTO approve(String themeId) {
        if (user.getRank().equals(UserRank.TEAM)) {
            if (repo.approveTheme(themeId)) {
                return new PanelResultDTO(OK);
            } else {
                return new PanelResultDTO(WARNING, "CUSTOM-THEMES.NOT-EXISTING");
            }
        }
        return new PanelResultDTO(UNPERMITTED);
    }
}
