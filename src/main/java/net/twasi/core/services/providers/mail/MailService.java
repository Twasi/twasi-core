package net.twasi.core.services.providers.mail;

import net.twasi.core.services.IService;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.TransportStrategy;

public class MailService implements IService {
    private Mailer mailer;

    private MailService() {
        mailer = new Mailer(
                ServiceRegistry.get(ConfigService.class).getCatalog().mail.server,
                ServiceRegistry.get(ConfigService.class).getCatalog().mail.port,
                ServiceRegistry.get(ConfigService.class).getCatalog().mail.user,
                ServiceRegistry.get(ConfigService.class).getCatalog().mail.password,
                TransportStrategy.SMTP_TLS
        );
        mailer.setDebug(false);
        mailer.trustAllSSLHosts(true);
    }

    public Mailer getMailer() {
        return mailer;
    }
}
