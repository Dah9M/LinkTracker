package com.github.service;

import com.github.TelegramSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@DisplayName("Unit test for SendMessageService")
public class SendMessageServiceTest {

    private SendMessageService sendMessageService;
    private TelegramLongPollingBot botMock;
    private TelegramSender telegramSender;

    @BeforeEach
    void setUp() throws Exception {
        telegramSender = Mockito.mock(TelegramSender.class);
        sendMessageService = new SendMessageService(telegramSender);

        botMock = Mockito.mock(TelegramLongPollingBot.class);

        Field botField = sendMessageService.getClass().getDeclaredField("bot");
        botField.setAccessible(true);
        botField.set(sendMessageService, botMock);
    }

    @Test
    void shouldProperlySendMessage() throws TelegramApiException {
        String chatId = "12345";
        String message = "Hello from test";

        sendMessageService.sendMessage(chatId, message);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(botMock).execute(captor.capture());

        SendMessage actualMsg = captor.getValue();
        assertEquals(chatId, actualMsg.getChatId());
        assertEquals(message, actualMsg.getText());
    }
}
