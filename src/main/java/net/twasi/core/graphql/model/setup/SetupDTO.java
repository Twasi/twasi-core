package net.twasi.core.graphql.model.setup;

import net.twasi.core.database.models.AccountStatus;
import net.twasi.core.database.models.BetaCode;
import net.twasi.core.database.models.User;
import net.twasi.core.database.repositories.BetaCodeRepository;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.graphql.TwasiGraphQLHandledException;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.InstanceManagerService;
import net.twasi.core.services.providers.config.ConfigService;

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
            code.setActivated(true);
            code.setUserId(user.getId().toString());
            repo.commit(code);
        }

        user.setBetaCode(betaCode);

        user.setStatus(AccountStatus.OK);
        user.getConfig().setActivated(true);

        ServiceRegistry.get(InstanceManagerService.class).start(user);
        ServiceRegistry.get(DataService.class).get(UserRepository.class).commit(user);

        return true;
    }

    public boolean isSetUp() {
        return user.getStatus() != AccountStatus.SETUP;
    }
}
