package net.twasi.core.database;

import net.twasi.core.config.Config;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.core.database.models.permissions.PermissionEntity;
import net.twasi.core.database.models.permissions.PermissionEntityType;
import net.twasi.core.database.models.permissions.PermissionGroups;
import net.twasi.core.database.models.permissions.Permissions;
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
            "3",
            Collections.singletonList(PermissionGroups.VIEWER)
    );

    private static List<Permissions> examplePermissions = new ArrayList<Permissions>();

    private static User user = new User();

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
                Arrays.asList("commands.add", "commands.edit", "commands.delete"),
                "commands"
        );
        permission.setName("commands");
        permission.setKeys(Arrays.asList("commands.add", "commands.edit", "commands.delete"));

        permission.setEntities(new ArrayList<>());

        PermissionEntity mods = new PermissionEntity();
        mods.setType(PermissionEntityType.GROUP);
        mods.setGroup(PermissionGroups.MODERATOR);
        PermissionEntity botDev = new PermissionEntity();
        botDev.setType(PermissionEntityType.SINGLE);
        botDev.setAccount(botDeveloper);
        permission.getEntities().add(mods);
        permission.getEntities().add(botDev);
        examplePermissions.add(permission);

        user.setPermissions(examplePermissions);
    }

    @Test
    public void grantsPermissionByGroup() {
        Boolean hasAddPermission = user.hasPermission(moderator, "commands.add");
        Boolean hasEditPermission = user.hasPermission(moderator, "commands.edit");
        Boolean hasDeletePermission = user.hasPermission(moderator, "commands.delete");

        Assert.assertTrue(hasAddPermission);
        Assert.assertTrue(hasEditPermission);
        Assert.assertTrue(hasDeletePermission);
    }

    @Test
    public void grantsPermissionByAccount() {
        Boolean hasAddPermission = user.hasPermission(botDeveloper, "commands.add");
        Boolean hasEditPermission = user.hasPermission(botDeveloper, "commands.edit");
        Boolean hasDeletePermission = user.hasPermission(botDeveloper, "commands.delete");

        Assert.assertTrue(hasAddPermission);
        Assert.assertTrue(hasEditPermission);
        Assert.assertTrue(hasDeletePermission);
    }

    @Test
    public void revokesPermission() {
        Boolean hasAddPermission = user.hasPermission(viewer, "commands.add");
        Boolean hasEditPermission = user.hasPermission(viewer, "commands.edit");
        Boolean hasDeletePermission = user.hasPermission(viewer, "commands.delete");

        Assert.assertFalse(hasAddPermission);
        Assert.assertFalse(hasEditPermission);
        Assert.assertFalse(hasDeletePermission);
    }
}
