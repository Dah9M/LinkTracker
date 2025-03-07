package com.github.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Service
public class SendMessageService implements SendMessageInterface {

    @Lazy
    @Autowired
    private TelegramLongPollingBot bot;

    public SendMessageService() {}

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);
        sm.setText(message);
        sm.enableHtml(true);

        try {
            bot.execute(sm);
        } catch (TelegramApiException e) {
            log.error("Failed to send message {} via Telegram API.", message);
            e.printStackTrace();
        }
    }

    @Override
    public void sendSubscriptions(String chatId, List<String> subscriptions) {
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);
        sm.enableHtml(true);
    }
}
