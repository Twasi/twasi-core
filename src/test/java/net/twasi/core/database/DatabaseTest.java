package net.twasi.core.database;

import net.twasi.core.config.Config;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class DatabaseTest {

    @BeforeClass
    public static void prepare() {
        Config.load();
        Database.connect();
    }

    @AfterClass
    public static void destruct() {
        Database.disconnect();
    }

    @Test
    public void saveUserTest() {

        User user = new User("test.user@domain.com");
        user.setTwitchAccount(new TwitchAccount("Larcce", "OAuth"));
        Database.getStore().save(user);

        List users = Database.getStore().createQuery(User.class).asList();
        System.out.println(Database.getMorphia().toDBObject(users.get(0)).toString());

        // assert statements
        //assertEquals("Creates User in Database", 0, Database.getStore().save(user));
        //assertEquals("0 x 10 must be 0", 0, tester.multiply(0, 10));
        //assertEquals("0 x 0 must be 0", 0, tester.multiply(0, 0));
    }

}
