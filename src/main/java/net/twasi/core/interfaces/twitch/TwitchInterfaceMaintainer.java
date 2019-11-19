package net.twasi.core.interfaces.twitch;

import net.twasi.core.logger.TwasiLogger;
import org.pircbotx.PircBotX;

import java.util.concurrent.TimeUnit;

public class TwitchInterfaceMaintainer extends Thread {

    private boolean run = true;
    private TwitchInterface twitchInterface;

    public TwitchInterfaceMaintainer(TwitchInterface twitchInterface) {
        TwasiLogger.log.debug("TwitchInterfaceMaintainer registered for user " + twitchInterface.getStreamer().getUser().getId());
        this.twitchInterface = twitchInterface;
        this.setDaemon(true); // No need to wait for a maintainer when a Twasi-stop is requested
    }

    @Override
    public void run() {
        while (true) {
            if (!run) {
                TwasiLogger.log.debug("TwitchInterfaceMaintainer stopped for user " + twitchInterface.getStreamer().getUser().getTwitchAccount().getUserName());
                return;
            }
            if (!twitchInterface.getBot().isConnected()) {
                TwasiLogger.log.debug("TwitchInterfaceMaintainer for user " + twitchInterface.getStreamer().getUser().getId() + " detected an unconnected TwitchInterface");
                new Thread(() -> {
                    PircBotX bot = twitchInterface.getBot();
                    try {
                        bot.stopBotReconnect();
                        if (bot.isConnected()) bot.close();
                    } catch (Exception ignored) {
                    } finally {
                        try {
                            bot.startBot();
                        } catch (Exception e) {
                            TwasiLogger.log.error("Unable to start bot for user " + twitchInterface.getStreamer().getUser().getId() + ": " + e.getMessage(), e);
                        }
                    }
                }).start();
            }
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (Exception e) {
                TwasiLogger.log.error(e);
            }
        }
    }

    public void stopMaintainer() {
        TwasiLogger.log.debug("TwitchInterfaceMaintainer stopped for user " + twitchInterface.getStreamer().getUser().getTwitchAccount().getUserName());
        run = false;
        this.twitchInterface.disconnect();
    }
}
