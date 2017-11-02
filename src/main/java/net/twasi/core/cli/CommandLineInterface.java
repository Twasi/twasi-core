package net.twasi.core.cli;

import net.twasi.core.logger.TwasiLogger;

import java.util.Scanner;

import org.apache.log4j.Level;

public class CommandLineInterface {

    public void start() {
        Scanner scanner = new Scanner(System.in);
        TwasiLogger.log.info("Started Twasi CLI. Use /help for a list of commands.");

        while(scanner.hasNextLine()) {
            System.out.print("> ");
            String input = scanner.nextLine();
            String[] params = input.split(" ");

            String command = params[0];

            switch (command) {
                case "/help":
                    System.out.println("Available commands:\n" +
                            "/help: Show all commands\n" +
                            "/loglevel: Show loglevel\n" +
                            "/loglevel [LOGLEVEL(OFF/FATAL/ERROR/WARN/INFO/DEBUG/TRACE/ALL)]: Set loglevel");
                    break;

                case "/version":
                    System.out.println("Not implemented");
                    break;

                case "/loglevel":
                    if (params.length == 1) {
                        System.out.println("Loglevel: " + TwasiLogger.log.getLevel().toString());
                    } else if (params.length == 2) {
                        TwasiLogger.setLogLevel(Level.toLevel(params[1]));
                        System.out.println("Loglevel changed to " + Level.toLevel(params[1]));
                    }
                    break;

                default:
                    System.out.println("Command not found. Use /help for help.");
            }
        };
    }

}
