package com.github.command;

import com.github.service.SendMessageInterface;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class StartCommand implements Command {

    private final SendMessageInterface sendMessageService;

    public final static String START_MESSAGE = "Привет. Я LinkTracker Bot. Я помогу тебе быть в курсе последних " +
            "обновлений тех ресурсов, которые тебе интересны.";

    public StartCommand(SendMessageInterface sendMessageService) {
        this.sendMessageService = sendMessageService;
    }
    @Override
    public void execute(Update update) throws TelegramApiException {
        sendMessageService.sendMessage(update.getMessage().getChatId().toString(), START_MESSAGE);
    }
}
