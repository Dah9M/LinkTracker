package service;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public interface SendMessageInterface {
    void sendMessage(String chatId, String message) throws TelegramApiException;
    void sendSubscriptions(String chatId, List<String> subscriptions);
}
