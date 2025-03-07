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

@DisplayName("Unit test for ListCommand")
public class ListCommandTest {

    private SubscriptionServiceInterface subscriptionService;
    private ListCommand listCommand;

    @BeforeEach
    public void init() {
        subscriptionService = Mockito.mock(SubscriptionServiceInterface.class);
        listCommand = new ListCommand(subscriptionService);
    }

    @Test
    public void shouldProperlyExecuteCommand() throws TelegramApiException {
        // given
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        String chatId = "12345";
        String text = "/list";

        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(12345L);
        when(message.getText()).thenReturn(text);

        // when
        listCommand.execute(update);

        // then
        verify(subscriptionService).getSubscriptions(chatId);
        verifyNoMoreInteractions(subscriptionService);
    }

    @Test
    public void shouldReturnCorrectCommandName() {
        assertEquals("/list", listCommand.getCommandName());
    }
}
