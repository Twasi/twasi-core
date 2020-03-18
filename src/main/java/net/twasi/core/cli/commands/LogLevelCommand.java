package net.twasi.core.cli.commands;

import net.twasi.core.cli.TwasiCLICommand;
import net.twasi.core.logger.TwasiLogger;
import org.apache.log4j.Level;

import java.util.List;

public class LogLevelCommand extends TwasiCLICommand {
    @Override
    public String getCommandName() {
        return "loglevel";
    }

    @Override
    public String getCommandHelptext() {
        return "This command can change the loglevel while Twasi runs.\n" +
                "Use either 'loglevel' to show the current loglevel or 'loglevel [level]'" +
                " to set a new loglevel.\nAvailable loglevels: [OFF/FATAL/ERROR/WARN/INFO/DEBUG/TRACE/ALL]";
    }

    @Override
    public String getCommandDescription() {
        return "Show or change the current loglevel.";
    }

    @Override
    public String execute(List<String> args) {
        if (args.size() == 0) {
            return "Loglevel: " + TwasiLogger.log.getLevel().toString();
        } else {
            try {
                TwasiLogger.setLogLevel(Level.toLevel(args.get(0)));
                return "Loglevel changed to " + Level.toLevel(args.get(0));
            } catch (Exception e) {
                return "Error while changing loglevel. Did you enter a valid level?";
            }
        }
    }
}
