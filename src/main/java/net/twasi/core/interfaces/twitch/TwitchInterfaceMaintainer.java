package net.twasi.core.interfaces.twitch;

import net.twasi.core.logger.TwasiLogger;

import java.util.concurrent.TimeUnit;

public class TwitchInterfaceMaintainer extends Thread {

    private boolean run = true;
    private TwitchInterface twitchInterface;

    public TwitchInterfaceMaintainer(TwitchInterface twitchInterface) {
        this.twitchInterface = twitchInterface;
        this.setDaemon(true); // No need to wait for a maintainer when a Twasi-stop is requested
    }

    @Override
    public void run() {
        while (true) {
            if (!run) return;
            if (!twitchInterface.getBot().isConnected()) {
                try {
                    twitchInterface.getBot().startBot();
                    TimeUnit.SECONDS.sleep(10);
                } catch (Exception e) {
                    TwasiLogger.log.error(e);
                }
            }
        }
    }

    public void stopMaintainer() {
        run = false;
    }
}
