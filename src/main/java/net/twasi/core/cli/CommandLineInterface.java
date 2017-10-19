package net.twasi.core.cli;

import net.twasi.core.logger.TwasiLogger;

import java.util.Scanner;

public class CommandLineInterface {

    public void start() {
        Scanner scanner = new Scanner(System.in);
        TwasiLogger.log.info("Started Twasi CLI. Use /help for a list of commands.");

        while(scanner.hasNextLine()) {
            System.out.print("> ");
            String input = scanner.nextLine();

            switch (input) {
                case "/help":
                    System.out.println("Available commands:\n" +
                            "/help: Show all commands\n" + "" +
                            "/loglevel: Show loglevel\n");
                    break;

                case "/version":
                    System.out.println("Not implemented");
                    break;

                case "/loglevel":
                    System.out.println("Loglevel: " + TwasiLogger.log.getLevel().toString());

                default:
                    System.out.println("Command not found. Use /help for help.");
            }
        };
    }

}
