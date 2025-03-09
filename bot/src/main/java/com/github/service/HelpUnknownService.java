package com.github.service;


import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.github.service.MessageName.*;

@Service
public class HelpUnknownService {

    private final SendMessageInterface sendMessageService;

    public HelpUnknownService(@Autowired SendMessageInterface sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    public void sendNoText(String chatId) throws TelegramApiException {
        sendMessageService.sendMessage(chatId, NO_MESSAGE.getMessageName());
    }

    public void sendUnknownText(String chatId) throws TelegramApiException {
        sendMessageService.sendMessage(chatId, UNTRACK_MESSAGE.getMessageName());
    }

    public void sendHelpText(String chatId) throws TelegramApiException {
        sendMessageService.sendMessage(chatId, HELP_MESSAGE.getMessageName());
    }
}
