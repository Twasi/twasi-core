package net.twasi.core.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.twasi.core.logger.TwasiLogger;
import org.apache.log4j.Level;

import java.util.Scanner;

public class CommandLineInterface {

    public void start() {
        Scanner scanner = new Scanner(System.in);
        TwasiLogger.log.info("Started Twasi CLI. Use /help for a list of commands.");
        System.out.print("> ");

        while (scanner.hasNextLine()) {
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

                case "/dump":
                case "/formdump":
                    Gson gson;
                    if (params[0].equals("/dump")) {
                        gson = new GsonBuilder().create();
                    } else {
                        gson = new GsonBuilder().setPrettyPrinting().create();
                    }
                    /* if (params.length == 1) {
                        System.out.println("Missing object");
                    } else if (params.length == 2) {
                        if (params[1].equalsIgnoreCase("users")) {
                            System.out.println(gson.toJson(UserStore.getUsers()));
                        } else {
                            System.out.println("Unknown object: " + params[1]);
                        }
                    }*/
                    break;

                default:
                    System.out.println("TwasiCommand not found. Use /help for help.");
            }
            System.out.print("> ");
        }
    }

}
