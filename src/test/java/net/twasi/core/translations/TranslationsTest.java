package net.twasi.core.translations;

import net.twasi.core.database.models.Language;
import net.twasi.core.database.models.User;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TranslationsTest {

    static User user;

    @BeforeClass
    public static void setUp() {
        user = new User();
    }

    @Test
    public void testTranslation() {
        user.getConfig().setLanguage(Language.DE_DE);

        TwasiTranslation translation = new TwasiTranslation(getClass().getClassLoader());

        Assert.assertEquals("unknown.translation.key", translation.getTranslation(user, "unknown.translation.key"));
        Assert.assertEquals("This string is only known by english language.", translation.getTranslation(user, "only.english.known"));
        Assert.assertEquals("Dieser Text ist nur hier verf&uuml;gbar.", translation.getTranslation(user, "only.german.known"));
        Assert.assertEquals("Bei beiden bekannt.", translation.getTranslation(user, "known.by.both"));
        Assert.assertEquals("Known by both.", translation.getTranslation(Language.EN_GB, "known.by.both"));

    }

}
