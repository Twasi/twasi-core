package net.twasi.core.services.providers;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.IService;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;
import net.twasi.core.services.providers.config.catalog.TelegramBotCatalog;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TelegramService implements IService {

    private TelegramLongPollingBot telegramBot = null;
    private TelegramBotCatalog config;

    private HashMap<String, TelegramBotCommandHandler> registeredCommands = new HashMap<>();

    public TelegramService() {

        config = ServiceRegistry.get(ConfigService.class).getCatalog().telegram;
        if (!config.enabled) return;
        // Load config and check whether telegram should be active or not

        if (config != null && config.botToken != null && config.chatId != 0 && config.username != null) {
            // Load Telegram config and check if settings are set

            telegramBot = new TelegramLongPollingBot() {
                @Override
                public String getBotToken() {
                    return config.botToken;
                }

                @Override
                public void onUpdateReceived(Update update) {

                    if (!update.hasMessage() || !update.getMessage().hasText()) return;
                    // If message has no text just ignore it

                    if (!update.getMessage().getChatId().equals(config.chatId)) {
                        SendMessage msg = new SendMessage(update.getMessage().getChatId(), "You are not permitted to use this bot.");
                        try {
                            telegramBot.execute(msg);
                        } catch (TelegramApiException e) {
                            TwasiLogger.log.error(e);
                        }
                        return;
                    }
                    // Ignore message if it's not sent from the chat-id in config

                    String text = update.getMessage().getText();
                    if (!text.startsWith("/")) return;
                    // If message is no command also ignore it

                    TwasiLogger.log.debug("Telegram command: " + text);

                    ArrayList<String> args = new ArrayList<>(Arrays.asList(text.split(" ")));
                    String command = args.get(0).substring(1);
                    args.remove(0);
                    // Get and split command name and arguments

                    if (registeredCommands.containsKey(command.toLowerCase())) {
                        TelegramBotCommandHandler handler = registeredCommands.get(command.toLowerCase());
                        // Search for registered command handler and get it if it exists

                        if (handler.getHelpFile().forceArgs() && args.size() < handler.getHelpFile().getArgNames().size()) {
                            try {
                                sendMessageToTelegramChat("Please enter the following command arguments:\n" + handler.getCommandHelpText());
                                // Force arguments if command handler says so

                            } catch (TelegramApiException e) {
                                TwasiLogger.log.debug("A telegram message could not be sent.");
                                TwasiLogger.log.error(e);
                            }
                            return;
                        }

                        handler.onCommand(command, args, update.getMessage(), TelegramService.this::sendMessageToTelegramChat);
                        // Trigger corresponding command handler
                    }
                }

                @Override
                public String getBotUsername() {
                    return config.username;
                }
            };
            try {
                new TelegramBotsApi().registerBot(telegramBot);
                TwasiLogger.log.info("Telegram bot is connected.");
                // Register bot and tell the user it is connected

            } catch (TelegramApiException e) {
                TwasiLogger.log.info("Telegram bot could not connect to the API.");
                TwasiLogger.log.error(e);
                telegramBot = null;
                return;
                // Set null to prevent usage of the bot while not connected

            }
        } else {
            TwasiLogger.log.info("Telegram options in config are uncompleted. Skipping Telegram login...");
            return;
        }
        try {
            sendMessageToTelegramChat("Twasi-Core connected.");
            // Give feedback to the user that everything is set up correctly

        } catch (TelegramApiException e) {
            TwasiLogger.log.info("Unable to send message to the Telegram chat that is set in config (\"" + config.chatId + "\").");
            TwasiLogger.log.error(e);
            telegramBot = null;
            return;
        }

        registerHelpCommandHandler();
    }

    public boolean isConnected() {
        return telegramBot != null;
    }

    public void sendMessageToTelegramChat(String message) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(config.chatId, message);
        telegramBot.execute(sendMessage);
    }

    public void registerCommandHandler(TelegramBotCommandHandler handler) {
        if (registeredCommands.containsKey(handler.getCommandName().toLowerCase())) {
            TwasiLogger.log.info("Tried to register Telegram bot command-handler but a handler with the name " + handler.getCommandName() + " already is registered.");
            return;
        }
        registeredCommands.put(handler.getCommandName().toLowerCase(), handler);
    }

    private void registerHelpCommandHandler() {
        registerCommandHandler(new TelegramBotCommandHandler() {
            @Override
            public String getCommandName() {
                return "help";
            }

            @Override
            public TelegramBotCommandHelpfile getHelpFile() {
                return new TelegramBotCommandHelpfile("Show this help");
            }

            @Override
            public void onCommand(String commandName, List<String> args, Message rawMessage, TelegramBotCommandHandlerAnswerInterface answerInterface) {
                StringBuilder stringBuilder = new StringBuilder("Commands:\n");
                for (TelegramBotCommandHandler handler : registeredCommands.values())
                    stringBuilder.append("\n").append(handler.getCommandHelpText()).append("\n");
                try {
                    answerInterface.answer(stringBuilder.toString());
                } catch (TelegramApiException e) {
                    TwasiLogger.log.info("A telegram message could not be sent.");
                    TwasiLogger.log.error(e);
                }
            }
        });

        /* // Example command handler:

        registerCommandHandler(new TelegramBotCommandHandler() {
            @Override
            public String getCommandName() {
                return "test";
            }

            @Override
            public TelegramBotCommandHelpfile getHelpFile() {
                return new TelegramBotCommandHelpfile("Just a command demonstration", true, "sexyArg1", "niceArg2", "coolArg3");
            }

            @Override
            public void onCommand(String commandName, List<String> args, Message rawMessage, TelegramBotCommandHandlerAnswerInterface answerInterface) {

            }
        });
        */
    }

    public abstract class TelegramBotCommandHandler {
        public abstract String getCommandName();
        // The command that should trigger the handler

        public abstract TelegramBotCommandHelpfile getHelpFile();
        // How a command is shown in help-file ("/setloglevel <level>" f.e.)

        public abstract void onCommand(String commandName, List<String> args, Message rawMessage, TelegramBotCommandHandlerAnswerInterface answerInterface);
        // The command event handler

        public String getCommandHelpText() {
            return "/" + getCommandName() + " " + getHelpFile().formatArgNames() + " - " + getHelpFile().getCommandDescription();
        }
        // Formats the command's help string
    }

    public class TelegramBotCommandHelpfile {
        private List<String> argNames;
        // The commands accepted arguments

        private String commandDescription;
        // A brief description of the command

        private boolean forceArgs;
        // Whether the bot should ask for all arguments or not

        public TelegramBotCommandHelpfile(String commandDescription, boolean forceArguments, String... argumentNames) {
            this.argNames = Arrays.asList(argumentNames);
            this.commandDescription = commandDescription;
            this.forceArgs = forceArguments;
        }

        public TelegramBotCommandHelpfile(String commandDescription) {
            this.argNames = new ArrayList<>();
            this.commandDescription = commandDescription;
            this.forceArgs = false;
        }

        public List<String> getArgNames() {
            return argNames;
        }

        public String formatArgNames() {
            if (argNames.size() == 0) return "";
            StringBuilder b = new StringBuilder();
            for (String s : argNames) b.append(" <").append(s).append(">");
            return b.toString().substring(1);
        }

        public String getCommandDescription() {
            return commandDescription;
        }

        public boolean forceArgs() {
            return forceArgs;
        }
    }

    public interface TelegramBotCommandHandlerAnswerInterface {
        void answer(String msg) throws TelegramApiException;
    }
}
