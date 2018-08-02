package net.twasi.core.services.providers.connector;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import net.twasi.core.common.Callback;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;

import java.io.IOException;
import java.util.List;

public class ConnectorController {
    private Channel controlChannel;
    private String codeName = ServiceRegistry.get(ConfigService.class).getCatalog().instance.codename;

    // Callback methods
    private Callback<Boolean> onConnectionCheck;
    private Callback<Boolean> onChannelCheck;

    public ConnectorController(Channel controlChannel) {
        this.controlChannel = controlChannel;

        DefaultConsumer consumer = new DefaultConsumer(controlChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String bodyString = new String(body, "UTF-8");
                String[] splitted = bodyString.split(" ");

                if (splitted[0].equalsIgnoreCase("hello")) {
                    String codeName = splitted[1];
                    String version = splitted[2];

                    TwasiLogger.log.info("Connected to Twasi-Twitch-Connector! Codename = " + codeName + ", Version = " + version);

                    onConnectionCheck.run(true);
                }
            }
        };

        try {
            controlChannel.queueDeclare(codeName, false, false, false, null);
            controlChannel.basicConsume(codeName, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkConnection(Callback<Boolean> cb) throws IOException {
        // Send hello
        controlChannel.basicPublish("", TwitchConnectorService.MASTER_CTRL_NAME, null, ("hello " + codeName + " Twasi-Core-Interface 0.1").getBytes());
        this.onConnectionCheck = cb;
    }

    public void checkChannels(List<IrcConnectionInfo> infos, Callback<Boolean> cb) throws IOException {
        // Send channels
        controlChannel.basicPublish("", TwitchConnectorService.MASTER_CTRL_NAME, null, ("check " + new Gson().toJson(infos)).getBytes());
        this.onChannelCheck = cb;
    }

}
