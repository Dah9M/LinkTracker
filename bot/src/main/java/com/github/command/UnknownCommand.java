package com.github.command;

import com.github.service.HelpUnknownService;
import com.github.service.SendMessageInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class UnknownCommand implements Command {

    private final HelpUnknownService helpUnknownService;

    @Autowired
    public UnknownCommand(HelpUnknownService helpUnknownService) {
        this.helpUnknownService = helpUnknownService;
    }


    @Override
    public void execute(Update update) throws TelegramApiException {
        String chatId = update.getMessage().getChatId().toString();

        helpUnknownService.sendUnknownText(chatId);
    }

    @Override
    public String getCommandName() {
        return "UNKNOWN";
    }
}
