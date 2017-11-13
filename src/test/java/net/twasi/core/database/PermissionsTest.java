package net.twasi.core.database;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.core.database.models.permissions.PermissionEntity;
import net.twasi.core.database.models.permissions.PermissionEntityType;
import net.twasi.core.database.models.permissions.PermissionGroups;
import net.twasi.core.database.models.permissions.Permissions;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PermissionsTest {

    // botDeveloper is Viewer and Subscriber. He has TwitchID 1.
    public TwitchAccount botDeveloper = new TwitchAccount() {{
        setGroups(new ArrayList<PermissionGroups>() {{
            add(PermissionGroups.VIEWER);
            add(PermissionGroups.SUBSCRIBERS);
        }});
        setTwitchId("1");
    }};
    // moderator is a moderator. He has TwitchID 2.
    public TwitchAccount moderator = new TwitchAccount() {{
        setGroups(new ArrayList<PermissionGroups>() {{
            add(PermissionGroups.VIEWER);
            add(PermissionGroups.SUBSCRIBERS);
            add(PermissionGroups.MODERATOR);
        }});
        setTwitchId("2");
    }};
    // viewer is just a viewer with TwitchID 3.
    public TwitchAccount viewer = new TwitchAccount() {{
        setGroups(new ArrayList<PermissionGroups>() {{
            add(PermissionGroups.VIEWER);
        }});
        setTwitchId("3");
    }};

    public List<Permissions> examplePermissions = new ArrayList<Permissions>() {{
        add(new Permissions() {{
            setName("Moderators and Bot Developer");
            setKeys(new ArrayList<String>() {{
                add("commands.add");
                add("commands.edit");
                add("commands.delete");
            }});
            setEntities(new ArrayList<PermissionEntity>() {{
                add(new PermissionEntity() {{
                    setGroup(PermissionGroups.MODERATOR);
                    setType(PermissionEntityType.GROUP);
                }});
                add(new PermissionEntity() {{
                    setAccount(botDeveloper);
                    setType(PermissionEntityType.SINGLE);
                }});
            }});
        }});
    }};

    public User user = new User() {{
        setPermissions(examplePermissions);
    }};

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
    public void revokesPermissionByGroup() {
        Boolean hasAddPermission = user.hasPermission(viewer, "commands.add");
        Boolean hasEditPermission = user.hasPermission(viewer, "commands.edit");
        Boolean hasDeletePermission = user.hasPermission(viewer, "commands.delete");

        Assert.assertFalse(hasAddPermission);
        Assert.assertFalse(hasEditPermission);
        Assert.assertFalse(hasDeletePermission);
    }
}
