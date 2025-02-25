package com.github.service;

import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class SendMessageService implements SendMessageInterface {

    private final TelegramLongPollingBot bot;

    public SendMessageService(@Autowired TelegramLongPollingBot bot) {
        this.bot = bot;
    }

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
}
