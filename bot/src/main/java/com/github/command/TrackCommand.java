package com.github.command;

import com.github.service.SendMessageInterface;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TrackCommand implements Command {

    private final SendMessageInterface sendMessageService;

    static final String TRACK_MESSAGE = "Начал отслеживание этого ресурса.";

    public TrackCommand(SendMessageInterface sendMessageService) {
        this.sendMessageService = sendMessageService;
    }


    @Override
    public void execute(Update update) throws TelegramApiException {
        sendMessageService.sendMessage(update.getMessage().getChatId().toString(), TRACK_MESSAGE);
    }
}
