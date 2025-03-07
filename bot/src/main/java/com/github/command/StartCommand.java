package com.github.command;

import com.github.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class StartCommand implements Command {

    private final UserService userService;

    public StartCommand(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void execute(Update update) throws TelegramApiException {
        String chatId = update.getMessage().getChatId().toString();
        userService.addUser(chatId);
    }

    @Override
    public String getCommandName() {
        return "/start";
    }
}
