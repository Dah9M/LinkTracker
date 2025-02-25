package com.github.service;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface SendMessageInterface {
    void sendMessage(String chatId, String message) throws TelegramApiException;
}
