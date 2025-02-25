package com.github.bot;

import com.github.command.CommandContainer;
import com.github.service.SendMessageInterface;
import com.github.service.SendMessageService;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.github.command.CommandName.NO;

@Slf4j
@Component
public class LinkTrackerBot extends TelegramLongPollingBot {

    private final static String COMMAND_PREFIX = "/";

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private final CommandContainer commandContainer;

    public LinkTrackerBot() {
        this.commandContainer = new CommandContainer(new SendMessageService(this));
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    // TODO: Понять как избавиться от SneakyThrows и почему здесь вообще кидает исключение
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();

            log.info("Received message: {}", message);

            if (message.startsWith(COMMAND_PREFIX)) {
                String commandIdentifier = message.split(" ")[0].toLowerCase();

                commandContainer.retrieveCommand(commandIdentifier).execute(update);
            } else {
                commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
            }

        }
    }
}
