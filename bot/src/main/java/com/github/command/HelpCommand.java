package com.github.command;

import com.github.service.HelpUnknownService;
import com.github.service.SendMessageInterface;
import com.github.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class HelpCommand implements Command {

    private final HelpUnknownService helpUnknownService;

    @Autowired
    public HelpCommand(HelpUnknownService helpUnknownService) {
        this.helpUnknownService = helpUnknownService;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        String chatId = update.getMessage().getChatId().toString();

        helpUnknownService.sendHelpText(chatId);
    }

    @Override
    public String getCommandName() {
        return "/help";
    }
}
