package net.twasi.core.interfaces.twitch;

import net.twasi.core.config.Config;
import net.twasi.core.interfaces.api.CommunicationHandler;
import net.twasi.core.interfaces.api.CommunicationHandlerInterface;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Message;
import net.twasi.core.models.Streamer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TwitchInterface extends TwasiInterface {

    CommunicationHandler handler = new CommunicationHandler() {
        @Override
        public boolean sendMessage(Message message) {
            return false;
        }
    };
    Streamer streamer;

    Socket socket;
    BufferedWriter writer;
    BufferedReader reader;

    public TwitchInterface(Streamer streamer) {
        this.streamer = streamer;
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
        TwasiLogger.log.debug("Connecting to Twitch IRC");
        try {
            socket = new Socket(Config.catalog.twitch.hostname, Config.catalog.twitch.port);
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.writer.write("PASS " + streamer.getUser().getTwitchBotAccountOrDefault().getToken() + "\r\n");
            this.writer.write("NICK " + streamer.getUser().getTwitchBotAccountOrDefault().getUserName() + "\r\n");
            this.writer.write("CAP REQ :twitch.tv/commands \r\n");
            this.writer.write("CAP REQ :twitch.tv/membership \r\n");
            this.writer.flush();

            String line = "";
            while ((line = this.reader.readLine()) != null) {
                if (line.contains("004")) {
                    TwasiLogger.log.debug("Connected: " + streamer.getUser().getTwitchAccount().getUserName() + " to " + Config.catalog.twitch.hostname + ":" + Config.catalog.twitch.port + "(Account: " + streamer.getUser().getTwitchBotAccountOrDefault().getUserName() + ")");
                    break;
                } else {
                    TwasiLogger.log.debug(line);
                }
            }

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
}
