package net.twasi.core.services.providers.connector;

import com.rabbitmq.client.Channel;
import net.twasi.core.common.Callback;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;

import java.io.IOException;

public class ConnectorController {
    private Channel controlChannel;

    public ConnectorController(Channel controlChannel) {
        this.controlChannel = controlChannel;
    }

    public void checkConnection(Callback<Boolean> cb) throws IOException {
        // Send hello
        controlChannel.basicPublish("", TwitchConnectorService.MASTER_CTRL_NAME, null, ("hello " + ServiceRegistry.get(ConfigService.class).getCatalog().instance.codename + " Twasi-Core-Interface 0.1").getBytes());
        cb.run(false);
    }

}
