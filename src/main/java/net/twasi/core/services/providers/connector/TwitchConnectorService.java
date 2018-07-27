package net.twasi.core.services.providers.connector;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import net.twasi.core.services.IService;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TwitchConnectorService implements IService {
    public static final String MASTER_CTRL_NAME = "MASTER_CTRL";
    public static final String MESSAGE_IN_NAME = "MESSAGE_IN";
    public static final String MESSAGE_OUT_NAME = "MESSAGE_OUT";

    private ConnectionFactory factory;
    private Connection connection;

    private Channel masterControlChannel;
    private Channel messageInChannel;
    private Channel messageOutChannel;

    public TwitchConnectorService() {

    }

    public void connect() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost(ServiceRegistry.getService(ConfigService.class).getCatalog().rabbitmq.hostname);
        factory.setPort(ServiceRegistry.getService(ConfigService.class).getCatalog().rabbitmq.port);
        factory.setUsername(ServiceRegistry.getService(ConfigService.class).getCatalog().rabbitmq.username);
        factory.setPassword(ServiceRegistry.getService(ConfigService.class).getCatalog().rabbitmq.password);
        connection = factory.newConnection();

        // Create and declare default channels
        masterControlChannel = connection.createChannel();
        masterControlChannel.queueDeclare(MASTER_CTRL_NAME, false, false, false, null);

        messageInChannel = connection.createChannel();
        messageInChannel.queueDeclare(MESSAGE_IN_NAME, false, false, false, null);

        messageOutChannel = connection.createChannel();
        messageOutChannel.queueDeclare(MESSAGE_OUT_NAME, false, false, false, null);
    }

}
