package com.github.command;

import com.github.service.SendMessageInterface;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class HelpCommand implements Command {

    private final SendMessageInterface sendMessageService;

    static final String HELP_MESSAGE = "/start - регистрация пользователя.\n" +
            "/help - вывод списка доступных команд.\n" +
            "/track - начать отслеживание ссылки.\n" +
            "/untrack - прекратить отслеживание ссылки.\n" +
            "/list - показать список отслеживаемых ссылок (cписок ссылок, полученных при /track)";

    public HelpCommand(SendMessageInterface sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        sendMessageService.sendMessage(update.getMessage().getChatId().toString(), HELP_MESSAGE);
    }
}
