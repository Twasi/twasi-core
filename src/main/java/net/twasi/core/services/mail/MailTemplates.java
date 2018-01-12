package net.twasi.core.services.mail;

import org.simplejavamail.email.Email;

public class MailTemplates {

    private static Email getBaseMail() {
        Email mail = new Email();
        mail.setFromAddress("Twasi Mailer", "noreply@twasi.net");

        return mail;
    }

    private static void appendFooter(Email mail) {
        mail.setText(mail.getText() + "\n\nDiese Email wurde automatisch generiert. Solltest du diese nicht erwartet haben kannst du sie entweder ignorieren oder uns dies mit einer Email an info@twasi.net melden.");
    }

    public static Email getEmailConfirmationMail(String email, String name, String confirmationCode) {
        Email mail = getBaseMail();

        mail.addNamedToRecipients(name, email);
        mail.setSubject("Willkommen bei Twasi");
        mail.setText("Hallo " + name + "!\n\n" +
                "Weiterer Content...\n");
        appendFooter(mail);

        return mail;
    }

}
