package net.twasi.core.services.mail;

import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.TransportStrategy;

public class MailService {

    private static MailService service = new MailService();

    public static MailService getService() {
        return service;
    }

    private Mailer mailer;

    private MailService() {
        mailer = new Mailer("mail.twasi.net", 587, "noreply@twasi.net", "PASSWORD", TransportStrategy.SMTP_TLS);
        mailer.setDebug(false);
        mailer.trustAllSSLHosts(true);
    }

    public Mailer getMailer() {
        return mailer;
    }
}
