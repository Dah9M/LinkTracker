package com.github.command;

import com.github.service.SendMessageInterface;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class UntrackCommand implements Command {

    private final SendMessageInterface sendMessageService;

    final static String UNTRACK_MESSAGE = "Деактивировал отслеживание этого ресурса.";

    public UntrackCommand(SendMessageInterface sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        sendMessageService.sendMessage(update.getMessage().getChatId().toString(), UNTRACK_MESSAGE);
    }
}
