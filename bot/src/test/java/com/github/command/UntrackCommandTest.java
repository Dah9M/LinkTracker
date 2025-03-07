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

@DisplayName("Unit test for UntrackCommand")
public class UntrackCommandTest {

    private SubscriptionServiceInterface subscriptionService;
    private UntrackCommand untrackCommand;

    @BeforeEach
    public void init() {
        subscriptionService = Mockito.mock(SubscriptionServiceInterface.class);
        untrackCommand = new UntrackCommand(subscriptionService);
    }

    @Test
    public void shouldProperlyExecuteCommand() throws TelegramApiException {
        // given
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        String chatId = "12345";
        String text = "/untrack https://github.com";

        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(12345L);
        when(message.getText()).thenReturn(text);

        // when
        untrackCommand.execute(update);

        // then
        verify(subscriptionService).deleteSubscription(chatId, new String[]{"/untrack", "https://github.com"});
        verifyNoMoreInteractions(subscriptionService);
    }

    @Test
    public void shouldReturnCorrectCommandName() {
        assertEquals("/untrack", untrackCommand.getCommandName());
    }
}
