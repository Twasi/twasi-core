package net.twasi.core.messages.internal;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Message.TwasiCommand;

import java.text.NumberFormat;
import java.util.stream.Collectors;

public class InternalCommandHandler {

    public boolean handle(TwasiCommand command) {
        if (!command.getTwasiInterface().getStreamer().getUser().hasPermission(command.getSender(), "twasi.admin")) {
            TwasiLogger.log.debug("User is not a global Twasi Admin. Checking for plugins to handle...");
            return false;
        }
        TwasiLogger.log.debug("User is a global Twasi Admin. Checking for internal commands...");
        if (command.getCommandName().equalsIgnoreCase("twasi")) {
            String[] split = command.getMessage().split(" ");
            if (split.length == 1) {
                String versionNumber = getClass().getPackage().getImplementationVersion();

                if (versionNumber == null) {
                    versionNumber = "LIVE";
                }

                command.reply("TwasiCore " + versionNumber);
            }
            if (split.length == 2) {
                if (split[1].equalsIgnoreCase("plugins")) {
                    String plugins = command.getTwasiInterface().getPlugins().stream().map(plugin -> plugin.getCorePlugin().toString()).collect(Collectors.joining());

                    command.reply("Installed plugins: " + plugins);
                }
                if (split[1].equalsIgnoreCase("ram")) {
                    Runtime runtime = Runtime.getRuntime();
                    NumberFormat format = NumberFormat.getInstance();

                    double usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
                    double totalMemory = runtime.totalMemory() / 1024 / 1024;
                    double maxMemory = runtime.maxMemory() / 1024 / 1024;

                    command.reply("RAM: " + format.format(usedMemory) + "MB / " + format.format(totalMemory) + "MB (Max: " + format.format(maxMemory) + "MB)");
                }
            }
            TwasiLogger.log.debug("Command was handled by internal command handler.");
            return true;
        }
        TwasiLogger.log.debug("Command was not handled by internal command handler. Checking for plugins to handle...");
        return false;
    }

}
