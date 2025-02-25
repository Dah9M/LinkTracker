package com.github.command;

import com.github.service.SendMessageInterface;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ListCommand implements Command {

    private final SendMessageInterface sendMessageService;

    final static String LIST_MESSAGE = "Список отслеживаемых ссылок: ";

    public ListCommand(SendMessageInterface sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        sendMessageService.sendMessage(update.getMessage().getChatId().toString(), LIST_MESSAGE);
    }

}
