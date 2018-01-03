package net.twasi.core.database;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.core.instances.InstanceManager;
import net.twasi.core.interfaces.api.CommunicationHandlerInterface;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.messages.MessageDispatcher;
import net.twasi.core.models.Streamer;
import org.junit.Assert;
import org.junit.Test;

import java.net.Socket;

public class InstanceManagerTest {
    @Test
    public void registerInterfaceTest() {
        InstanceManager instanceManager = new InstanceManager();
        User user = new User();
        user.setTwitchAccount(new TwitchAccount());

        TwasiInterface interfaceOne = new TwasiInterface() {
            @Override
            public void onEnable() {

            }

            @Override
            public void onDisable() {

            }

            @Override
            public boolean connect() {
                return false;
            }

            @Override
            public boolean disconnect() {
                return false;
            }

            @Override
            public boolean join(Streamer streamer) {
                return false;
            }

            @Override
            public CommunicationHandlerInterface getCommunicationHandler() {
                return null;
            }

            @Override
            public Streamer getStreamer() {
                return new Streamer(user);
            }

            @Override
            public MessageDispatcher getDispatcher() {
                return null;
            }

            @Override
            public Socket getSocket() {
                return null;
            }
        };
        TwasiInterface interfaceTwo = new TwasiInterface() {
            @Override
            public void onEnable() {

            }

            @Override
            public void onDisable() {

            }

            @Override
            public boolean connect() {
                return false;
            }

            @Override
            public boolean disconnect() {
                return false;
            }

            @Override
            public boolean join(Streamer streamer) {
                return false;
            }

            @Override
            public CommunicationHandlerInterface getCommunicationHandler() {
                return null;
            }

            @Override
            public Streamer getStreamer() {
                return new Streamer(user);
            }

            @Override
            public MessageDispatcher getDispatcher() {
                return null;
            }

            @Override
            public Socket getSocket() {
                return null;
            }
        };

        instanceManager.registerInterface(interfaceOne);
        instanceManager.registerInterface(interfaceTwo);
        boolean alreadyRegistered = instanceManager.registerInterface(interfaceOne);

        Assert.assertEquals(instanceManager.getInterfaces().size(), 2);
        Assert.assertFalse(alreadyRegistered);
    }

}
