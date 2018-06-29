package net.twasi.core.database;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.core.database.models.permissions.PermissionEntity;
import net.twasi.core.database.models.permissions.PermissionEntityType;
import net.twasi.core.database.models.permissions.PermissionGroups;
import net.twasi.core.database.models.permissions.Permissions;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PermissionsTest {

    // botDeveloper is Viewer and Subscriber. He has TwitchID 1.
    private static TwitchAccount botDeveloper = new TwitchAccount(
            "BotDeveloper",
            null,
            null,
            "1",
            Arrays.asList(
                    PermissionGroups.VIEWER,
                    PermissionGroups.SUBSCRIBERS
            )
    );
    // moderator is a moderator. He has TwitchID 2.
    private static TwitchAccount moderator = new TwitchAccount(
            "MyCoolModerator",
            null,
            null,
            "2",
            Arrays.asList(
                    PermissionGroups.VIEWER,
                    PermissionGroups.SUBSCRIBERS,
                    PermissionGroups.MODERATOR
            )
    );
    // viewer is just a viewer with TwitchID 3.
    private static TwitchAccount viewer = new TwitchAccount(
            "MyKindViewer",
            null,
            null,
            "3",
            Collections.singletonList(PermissionGroups.VIEWER)
    );

    private static List<Permissions> examplePermissions = new ArrayList<Permissions>();

    private static User user;

    @BeforeClass
    public static void setUp() {
        Permissions permission = new Permissions(
                Arrays.asList(
                        new PermissionEntity(
                                PermissionEntityType.GROUP,
                                PermissionGroups.MODERATOR,
                                null
                        ),
                        new PermissionEntity(
                                PermissionEntityType.SINGLE,
                                null,
                                botDeveloper
                        )
                ),
                Arrays.asList("commands.mod.*", "commands.user.execute"),
                "commands"
        );
        examplePermissions.add(permission);
        user = new User();

        user.setPermissions(examplePermissions);
    }

    @Test
    public void grantsPermissionByGroup() {
        checkForUser(moderator);
    }

    @Test
    public void grantsPermissionByAccount() {
        checkForUser(botDeveloper);
    }

    private void checkForUser(TwitchAccount moderator) {
        Boolean hasAddPermission = user.hasPermission(moderator, "commands.mod.add");
        Boolean hasEditPermission = user.hasPermission(moderator, "commands.mod.edit");
        Boolean hasDeletePermission = user.hasPermission(moderator, "commands.mod.delete");
        Boolean hasUnknownPermission = user.hasPermission(moderator, "commands.unknown.doNothing");

        Assert.assertTrue(hasAddPermission);
        Assert.assertTrue(hasEditPermission);
        Assert.assertTrue(hasDeletePermission);
        Assert.assertFalse(hasUnknownPermission);
    }

    @Test
    public void revokesPermission() {
        Boolean hasAddPermission = user.hasPermission(viewer, "commands.add");
        Boolean hasEditPermission = user.hasPermission(viewer, "commands.edit");
        Boolean hasDeletePermission = user.hasPermission(viewer, "commands.delete");
        Boolean hasUnknownPermission = user.hasPermission(moderator, "commands.unknown.doNothing");

        Assert.assertFalse(hasAddPermission);
        Assert.assertFalse(hasEditPermission);
        Assert.assertFalse(hasDeletePermission);
        Assert.assertFalse(hasUnknownPermission);
    }
}
