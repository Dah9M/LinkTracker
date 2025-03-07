package com.github.command;

import com.github.service.SubscriptionServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Unit test for TrackCommand")
public class TrackCommandTest {

    private SubscriptionServiceInterface subscriptionService;
    private TrackCommand trackCommand;

    @BeforeEach
    public void init() {
        subscriptionService = Mockito.mock(SubscriptionServiceInterface.class);
        trackCommand = new TrackCommand(subscriptionService);
    }

    @Test
    public void shouldProperlyExecuteCommand() throws TelegramApiException {
        // given
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        String chatId = "12345";
        String text = "/track https://github.com";

        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(12345L);
        when(message.getText()).thenReturn(text);

        // when
        trackCommand.execute(update);

        // then
        verify(subscriptionService).addSubscription(chatId, new String[]{"/track", "https://github.com"});
        verifyNoMoreInteractions(subscriptionService);
    }

    @Test
    public void shouldReturnCorrectCommandName() {
        assertEquals("/track", trackCommand.getCommandName());
    }
}
