package net.twasi.core.webinterface.controller.confirm;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.config.Config;
import net.twasi.core.database.Database;
import net.twasi.core.database.models.AccountStatus;
import net.twasi.core.database.models.User;
import net.twasi.core.database.store.UserStore;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfirmController extends RequestHandler {

    @Override
    public void handleGet(HttpExchange t) {
        t.getResponseHeaders().set("Content-Type", "text/html");

        Map<String, String> params = Commons.parseQueryParams(t);

        if (!params.containsKey("code")) {
            Commons.writeString(t, "Kein Code gefunden. Bitte versuche es erneut, oder fordere im <a href=\"" + Config.getCatalog().webinterface.frontend + "\">Panel</a> eine neue E-Mail an.", 400);
            return;
        }

        String code = params.get("code");

        //List<User> availableUsers = Database.getStore().createQuery(User.class).field("twitchAccount.confirmationCode").equal(code).asList();
        List<User> availableUsers = UserStore.getUsers().stream().filter(user -> user.getTwitchAccount().getConfirmationCode().equals(code)).collect(Collectors.toList());

        if (availableUsers.size() != 1) {
            Commons.writeString(t, "Dieser Code ist nicht korrekt. Bitte versuche es erneut, oder fordere im <a href=\"" + Config.getCatalog().webinterface.frontend + "\">Panel</a> eine neue E-Mail an.", 400);
            return;
        }

        User user = availableUsers.get(0);
        user.setStatus(AccountStatus.OK);
        Database.getStore().save(user);
        Commons.writeRedirect(t, Config.getCatalog().webinterface.frontend);
    }
}
