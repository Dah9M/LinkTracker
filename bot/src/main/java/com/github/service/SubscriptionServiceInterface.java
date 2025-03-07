package com.github.service;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface SubscriptionServiceInterface {
    void addSubscription(String chatId, String[] message) throws TelegramApiException;
    void deleteSubscription(String chatId, String[] message) throws TelegramApiException;
    void getSubscriptions(String chatId) throws TelegramApiException;
}
