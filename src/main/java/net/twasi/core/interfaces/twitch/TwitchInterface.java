package net.twasi.core.interfaces.twitch;

import net.twasi.core.interfaces.MessageReader;
import net.twasi.core.interfaces.api.CommunicationHandler;
import net.twasi.core.interfaces.api.CommunicationHandlerInterface;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.messages.MessageDispatcher;
import net.twasi.core.models.Streamer;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TwitchInterface extends TwasiInterface {

    private CommunicationHandler handler;
    private Streamer streamer;

    private PircBotX bot;

    private MessageReader messageReader;

    // Dispatcher for incoming messages
    private MessageDispatcher dispatcher;

    public TwitchInterface(Streamer streamer) {
        super(streamer.getUser());
        this.streamer = streamer;

        this.handler = new TwitchCommunicationHandler(this);
        this.dispatcher = new MessageDispatcher(this);
    }

    @Override
    public void onEnable() {
        TwasiLogger.log.debug("Enabling Twitch Interface.");
    }

    @Override
    public void onDisable() {
        TwasiLogger.log.debug("Disabling Twitch Interface.");
    }

    @Override
    public boolean connect() {
        try {
            this.messageReader = new MessageReader(this);

            //Configure what we want our bot to do
            Configuration configuration = new Configuration.Builder()
                    //.setLogin(streamer.getUser().getTwitchBotAccountOrDefault().getUserName())
                    .setName(streamer.getUser().getTwitchBotAccountOrDefault().getUserName())
                    .setServerPassword(streamer.getUser().getTwitchBotAccountOrDefault().getToken().getAccessToken())
                    //.setNickservPassword(streamer.getUser().getTwitchBotAccountOrDefault().getToken().getAccessToken())
                    .addServer(ServiceRegistry.get(ConfigService.class).getCatalog().twitch.hostname)
                    .addAutoJoinChannel(streamer.getUser().getTwitchAccount().getChannel())
                    .addCapHandler(new EnableCapHandler("twitch.tv/commands"))
                    .addCapHandler(new EnableCapHandler("twitch.tv/membership"))
                    .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
                    .addListener(new ListenerAdapter() {
                        @Override
                        public void onConnect(ConnectEvent event) throws Exception {
                            TwasiLogger.log.debug("Connected to IRC: " + streamer.getUser().getTwitchAccount().getDisplayName() + " - " + ServiceRegistry.get(ConfigService.class).getCatalog().twitch.hostname);
                        }

                        @Override
                        public void onDisconnect(DisconnectEvent event) throws Exception {
                            TwasiLogger.log.debug("Disconnected from IRC: " + streamer.getUser().getTwitchAccount().getDisplayName() + " - " + ServiceRegistry.get(ConfigService.class).getCatalog().twitch.hostname);
                        }

                        /* @Override
                        public void onMessage(MessageEvent event) throws Exception {
                            TwasiLogger.log.debug("Incoming message: " + event.getMessage());
                            messageReader.onMessage(event);
                        } */

                        @Override
                        public void onEvent(Event event) {
                            if (event instanceof MessageEvent) {
                                MessageEvent messageEvent = (MessageEvent) event;
                                TwasiLogger.log.debug("Incoming message: " + messageEvent.getMessage());
                                messageReader.onMessage(messageEvent);
                            }
                            //TwasiLogger.log.debug("New event: " + event.toString());
                        }
                    })
                    .buildConfiguration();

            //Create our bot with the configuration
            bot = new PircBotX(configuration);
            //Connect to the server

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        bot.startBot();
                    } catch (Exception e) {
                        TwasiLogger.log.error(e);
                        e.printStackTrace();
                    }
                }
            });
            t.start();

            /* socket = new Socket(ServiceRegistry.get(ConfigService.class).getCatalog().twitch.hostname, ServiceRegistry.get(ConfigService.class).getCatalog().twitch.port);
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.writer.write("PASS " + streamer.getUser().getTwitchBotAccountOrDefault().getToken().getAccessToken() + "\n");
            this.writer.write("NICK " + streamer.getUser().getTwitchBotAccountOrDefault().getUserName() + "\n");
            this.writer.write("CAP REQ :twitch.tv/commands\n");
            this.writer.write("CAP REQ :twitch.tv/membership\n");
            this.writer.write("CAP REQ :twitch.tv/tags\n");
            this.writer.write("JOIN " + streamer.getUser().getTwitchAccount().getChannel() + "\n");
            this.writer.flush();

            String line = "";
            while ((line = this.reader.readLine()) != null) {
                if (line.contains("004")) {
                    TwasiLogger.log.debug("Connected: " + streamer.getUser().getTwitchAccount().getUserName() + " to " + ServiceRegistry.get(ConfigService.class).getCatalog().twitch.hostname + ":" + ServiceRegistry.get(ConfigService.class).getCatalog().twitch.port + " (Account: " + streamer.getUser().getTwitchBotAccountOrDefault().getUserName() + ")");
                    break;
                } else {
                    TwasiLogger.log.trace(line);
                }
            }

            this.messageReader = new Thread(new MessageReader(this));
            this.messageReader.start();*/

            return true;
        } catch (Exception e) {
            TwasiLogger.log.error("Failed to connect to Twitch IRC: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean disconnect() {
        TwasiLogger.log.info("Disconnecting from Twitch IRC");
        try {
            bot.stopBotReconnect();
            bot.sendIRC().quitServer("Bye, thanks for your service @Twitch #Twasi");
            return true;
        } catch (Exception e) {
            TwasiLogger.log.error("Failed to disconnect from Twitch IRC: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean join(Streamer streamer) {
        TwasiLogger.log.debug("Joining channel for Streamer " + streamer.getUser().getTwitchAccount().getUserName());
        return false;
    }

    @Override
    public CommunicationHandlerInterface getCommunicationHandler() {
        return handler;
    }

    @Override
    public Streamer getStreamer() {
        return streamer;
    }

    @Override
    public MessageDispatcher getDispatcher() {
        return dispatcher;
    }

    public PircBotX getBot() {
        return bot;
    }
}
