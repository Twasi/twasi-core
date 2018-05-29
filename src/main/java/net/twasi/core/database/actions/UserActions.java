package net.twasi.core.database.actions;

import net.twasi.core.database.models.AccountStatus;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.config.ConfigService;
import net.twasi.core.services.providers.mail.MailService;
import net.twasi.core.services.providers.mail.MailTemplates;

import java.util.UUID;

public class UserActions {

    public static User createNewUser(TwitchAccount account) {
        User user = new User();
        user.setTwitchAccount(account);
        user.setStatus(AccountStatus.SETUP);

        if (ServiceRegistry.get(ConfigService.class).getCatalog().mail.enabled) {
            String confirmationCode = user.getTwitchAccount().getUserName() + "_" + UUID.randomUUID().toString();
            user.getTwitchAccount().setConfirmationCode(confirmationCode);

            // Send welcome mail
            ServiceRegistry.get(MailService.class).getMailer().sendMail(MailTemplates.getEmailConfirmationMail(user.getTwitchAccount().getEmail(), user.getTwitchAccount().getUserName(), confirmationCode));
        }

        ServiceRegistry.get(DataService.class).get(UserRepository.class).add(user);
        return user;
    }

}
