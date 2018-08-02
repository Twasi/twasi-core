package net.twasi.core.services.providers.connector;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import net.twasi.core.services.IService;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class TwitchConnectorService implements IService {
    public static final String MASTER_CTRL_NAME = "MASTER_CTRL_TOCONNECTOR";
    public static final String MASTER_CTRL_RCV_NAME = "MASTER_CTRL_TOCORE";
    public static final String MESSAGE_IN_NAME = "MESSAGE_IN";
    public static final String MESSAGE_OUT_NAME = "MESSAGE_OUT";

    private ConnectionFactory factory;
    private Connection connection;

    private Channel channel;

    private ConnectorController controller;

    public TwitchConnectorService() {

    }

    public void connect() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost(ServiceRegistry.getService(ConfigService.class).getCatalog().rabbitmq.hostname);
        factory.setPort(ServiceRegistry.getService(ConfigService.class).getCatalog().rabbitmq.port);
        factory.setUsername(ServiceRegistry.getService(ConfigService.class).getCatalog().rabbitmq.username);
        factory.setPassword(ServiceRegistry.getService(ConfigService.class).getCatalog().rabbitmq.password);
        connection = factory.newConnection();

        // Create default channel
        channel = connection.createChannel();

        // Declare queues
        channel.queueDeclare(MASTER_CTRL_NAME, false, false, false, null);
        channel.queueDeclare(MASTER_CTRL_RCV_NAME, false, false, false, null);
        channel.queueDeclare(MESSAGE_IN_NAME, false, false, false, null);
        channel.queueDeclare(MESSAGE_OUT_NAME, false, false, false, null);

        // Check connection
        controller = new ConnectorController(channel);
        controller.checkConnection(bool -> System.out.println(bool));

        List<IrcConnectionInfo> channels = new ArrayList<>();
        channels.add(new IrcConnectionInfo("7777777", "Larcce"));
        channels.add(new IrcConnectionInfo("77777732", "Test", "DieserBot", "oauth:daöasdfjsadölfj"));

        controller.checkChannels(channels, bool -> System.out.println(bool));
    }

}
