package net.twasi.core.interfaces.twitch;

import net.twasi.core.config.Config;
import net.twasi.core.interfaces.MessageReader;
import net.twasi.core.interfaces.api.CommunicationHandler;
import net.twasi.core.interfaces.api.CommunicationHandlerInterface;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.messages.MessageDispatcher;
import net.twasi.core.models.Streamer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TwitchInterface extends TwasiInterface {

    private CommunicationHandler handler;
    private Streamer streamer;

    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    private Thread messageReader;

    // Dispatcher for incoming messages
    private MessageDispatcher dispatcher;

    public TwitchInterface(Streamer streamer) {
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
            socket = new Socket(Config.getCatalog().twitch.hostname, Config.getCatalog().twitch.port);
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
                    TwasiLogger.log.debug("Connected: " + streamer.getUser().getTwitchAccount().getUserName() + " to " + Config.getCatalog().twitch.hostname + ":" + Config.getCatalog().twitch.port + " (Account: " + streamer.getUser().getTwitchBotAccountOrDefault().getUserName() + ")");
                    break;
                } else {
                    TwasiLogger.log.trace(line);
                }
            }

            this.messageReader = new Thread(new MessageReader(this));
            this.messageReader.start();

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
            writer.close();
            reader.close();
            socket.close();
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

    @Override
    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public BufferedWriter getWriter() {
        return writer;
    }
}
