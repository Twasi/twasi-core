package net.twasi.core.services.providers;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.IService;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;
import net.twasi.core.services.providers.config.catalog.TelegramBotCatalog;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramService implements IService {

    private TelegramLongPollingBot telegramBot = null;
    private TelegramBotCatalog config;

    public TelegramService() {
        config = ServiceRegistry.get(ConfigService.class).getCatalog().telegram;
        if (config != null && config.botToken != null && config.groupId != null && config.username != null) {
            telegramBot = new TelegramLongPollingBot() {
                @Override
                public String getBotToken() {
                    return config.botToken;
                }

                @Override
                public void onUpdateReceived(Update update) {
                    // Ignoring incoming messages
                }

                @Override
                public String getBotUsername() {
                    return config.username;
                }
            };
            try {
                ApiContextInitializer.init();
                new TelegramBotsApi().registerBot(telegramBot);
                TwasiLogger.log.info("Telegram bot is connected.");
            } catch (TelegramApiException e) {
                TwasiLogger.log.info("Telegram bot could not connect to the API.");
                TwasiLogger.log.debug(e);
                return;
            }
        } else {
            TwasiLogger.log.info("Telegram options in config are uncompleted. Skipping Telegram login...");
            return;
        }
        try {
            sendMessageToTelegramGroup("Twasi-Core connected.");
        } catch (TelegramApiException e) {
            TwasiLogger.log.info("Unable to send message to the Telegram group that is set in config.");
            TwasiLogger.log.debug(e);
            telegramBot = null;
        }
    }

    public boolean isConnected() {
        return telegramBot != null;
    }

    public void sendMessageToTelegramGroup(String message) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(config.groupId, message);
        telegramBot.execute(sendMessage);
    }

}
