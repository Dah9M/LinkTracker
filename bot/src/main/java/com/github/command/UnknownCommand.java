package com.github.command;

import com.github.service.SendMessageInterface;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class UnknownCommand implements Command {

    private final SendMessageInterface sendMessageService;

    public UnknownCommand(SendMessageInterface sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    public static final String UNKNOWN_MESSAGE = "Не понимаю вас, напишите /help чтобы узнать что я понимаю.";


    @Override
    public void execute(Update update) throws TelegramApiException {
        sendMessageService.sendMessage(update.getMessage().getChatId().toString(), UNKNOWN_MESSAGE);
    }
}
