package com.github.service;

import com.github.bot.LinkTrackerBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@DisplayName("Unit test for SendMessageService")
public class SendMessageServiceTest {

    private SendMessageService sendBotMessageService;
    private LinkTrackerBot bot;

    @BeforeEach
    public void init() {
        bot = Mockito.mock(LinkTrackerBot.class);
        sendBotMessageService = new SendMessageService(bot);
    }

    @Test
    public void shouldProperlySendMessage() throws TelegramApiException {
        //given
        Long chatId = 123L;
        String message = "test_message";

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(message);
        sendMessage.setChatId(chatId.toString());
        sendMessage.enableHtml(true);

        //when
        sendBotMessageService.sendMessage(chatId.toString(), message);

        //then
        Mockito.verify(bot).execute(sendMessage);
    }
}
