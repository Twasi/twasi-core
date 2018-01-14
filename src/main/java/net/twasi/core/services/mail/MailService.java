package net.twasi.core.services.mail;

import net.twasi.core.config.Config;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.TransportStrategy;

public class MailService {

    private static MailService service = new MailService();

    public static MailService getService() {
        return service;
    }

    private Mailer mailer;

    private MailService() {
        mailer = new Mailer(Config.getCatalog().mail.server, Config.getCatalog().mail.port, Config.getCatalog().mail.user, Config.getCatalog().mail.password, TransportStrategy.SMTP_TLS);
        mailer.setDebug(false);
        mailer.trustAllSSLHosts(true);
    }

    public Mailer getMailer() {
        return mailer;
    }
}
