package com.github.command;

import core.TelegramSender;
import bot.LinkTrackerBot;
import service.SendMessageService;
import command.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.Field;

abstract class AbstractCommandTest {
    // Мок‐бот, на который будет вызываться execute(...)
    protected LinkTrackerBot bot = Mockito.mock(LinkTrackerBot.class);
    protected TelegramSender telegramSender = Mockito.mock(TelegramSender.class);
    protected SendMessageService sendMessageService = new SendMessageService(telegramSender);

    abstract String getCommandName();
    abstract String getCommandMessage();
    abstract Command getCommand();

    @BeforeEach
    void setUpBotInService() throws Exception {
        Field botField = sendMessageService.getClass().getDeclaredField("bot");
        botField.setAccessible(true);
        botField.set(sendMessageService, bot);
    }

    @Test
    public void shouldProperlyExecuteCommand() throws TelegramApiException {
        Long chatId = 1234567824356L;

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(message.getText()).thenReturn(getCommandName());
        update.setMessage(message);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(getCommandMessage());
        sendMessage.enableHtml(true);

        getCommand().execute(update);

        Mockito.verify(bot).execute(sendMessage);
    }
}
