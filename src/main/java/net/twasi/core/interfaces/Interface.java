package net.twasi.core.interfaces;

import net.twasi.core.database.Database;
import net.twasi.core.database.models.User;
import net.twasi.core.interfaces.twitch.TwitchInterface;
import net.twasi.core.models.Streamer;

public class Interface {

    static InterfaceLoader loader;

    public static void load() {
        loader = new InterfaceLoader();
        /* TwitchInterface bot = loader.createNewTwitch(new Streamer(Database.getStore().createQuery(User.class).asList().get(0)));
        bot.connect(); */
    }

}
