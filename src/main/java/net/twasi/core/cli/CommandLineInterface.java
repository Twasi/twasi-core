package net.twasi.core.cli;

import net.twasi.core.cli.commands.DumpCommand;
import net.twasi.core.cli.commands.HelpCommand;
import net.twasi.core.cli.commands.LogLevelCommand;
import net.twasi.core.cli.commands.VersionCommand;
import net.twasi.core.logger.TwasiLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CommandLineInterface {

    private static List<TwasiCLICommand> commands = new ArrayList<>();

    public static void registerCommand(TwasiCLICommand cmd) {
        if(commands.stream().anyMatch(c -> cmd.getCommandName().equalsIgnoreCase(c.getCommandName()))) {
            TwasiLogger.log.warn(String.format("Tried to register cli-command '%s' multiple times. Skipping.", cmd.getCommandName().toLowerCase()));
            return;
        }
        commands.add(cmd);
    }

    public void start() {
        registerDefaultCommands();

        Scanner scanner = new Scanner(System.in);
        TwasiLogger.log.info("Started Twasi CLI. Use 'help' for a list of commands.");

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            String[] params = input.split(" ");

            String command = params[0];

            TwasiCLICommand cmd = commands.stream().filter(c -> c.getCommandName().equalsIgnoreCase(command)).findFirst().orElse(null);
            if(cmd == null) {
                System.out.println("TwasiCommand not found. Use 'help' for help.");
                return;
            }

            List<String> args = new ArrayList<>(Arrays.asList(params));
            args.remove(0);

            try {
                System.out.println(cmd.execute(args));
            } catch (Exception e) {
                System.out.println("An error occurred while executing the command.");
                TwasiLogger.log.debug(e);
            }
        }
    }

    private void registerDefaultCommands() {
        registerCommand(new HelpCommand(commands));
        registerCommand(new DumpCommand());
        registerCommand(new VersionCommand());
        registerCommand(new LogLevelCommand());
    }

}
