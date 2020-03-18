package net.twasi.core.graphql.model.setup;

import net.twasi.core.database.models.AccountStatus;
import net.twasi.core.database.models.BetaCode;
import net.twasi.core.database.models.User;
import net.twasi.core.database.repositories.BetaCodeRepository;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.graphql.TwasiGraphQLHandledException;
import net.twasi.core.graphql.model.PluginDetailsDTO;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.InstanceManagerService;
import net.twasi.core.services.providers.PluginManagerService;
import net.twasi.core.services.providers.TelegramService;
import net.twasi.core.services.providers.config.ConfigService;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SetupDTO {
    private User user;

    public SetupDTO(User user) {
        this.user = user;
    }

    public boolean setup(String betaCode) {
        if (isSetUp()) {
            throw new TwasiGraphQLHandledException("Your account is already set up.", "setup_already_done");
        }

        boolean isMasterKey = betaCode.equals(ConfigService.get().getCatalog().auth.masterBetaKey);

        if (!isMasterKey) {
            BetaCodeRepository repo = ServiceRegistry.get(DataService.class).get(BetaCodeRepository.class);

            // validate code
            BetaCode code = repo.getByCode(betaCode);

            if (code == null) {
                throw new TwasiGraphQLHandledException("The beta code you entered is not valid", "setup_invalid_code");
            }

            if (code.isActivated()) {
                throw new TwasiGraphQLHandledException("The beta code you entered was already redeemed.", "setup_invalid_code");
            }

            // invalidate beta code
            code.setUnlocked(new Date());
            code.setUserId(user.getId().toString());
            repo.commit(code);
        }

        user.setBetaCode(betaCode);

        user.setStatus(AccountStatus.OK);
        user.getConfig().setActivated(true);

        ServiceRegistry.get(InstanceManagerService.class).start(user);
        ServiceRegistry.get(DataService.class).get(UserRepository.class).commit(user);

        TelegramService telegram = TelegramService.get();
        if (telegram.isConnected()) {
            try {
                telegram.sendMessageToTelegramChat(
                        "( ͡° ͜ʖ ͡°) Ein neuer Benutzer hat sich soeben registriert: " +
                                user.getTwitchAccount().getDisplayName()
                );
            } catch (TelegramApiException ignored) {
            }
        }

        return true;
    }

    public boolean isSetUp() {
        return user.getStatus() != AccountStatus.SETUP;
    }

    public List<PluginDetailsDTO> getDefaultPlugins() {
        PluginManagerService pms = PluginManagerService.get();
        List<String> defaults = pms.getDefaultPlugins();
        return pms.getPlugins().stream().filter(pl -> defaults.contains(pl.getName())).map(
                pl -> new PluginDetailsDTO(pl, user, user.getInstalledPlugins().contains(pl.getName()))
        ).collect(Collectors.toList());
    }
}
