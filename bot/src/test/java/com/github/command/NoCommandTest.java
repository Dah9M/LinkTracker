package com.github.command;

import com.github.service.HelpUnknownService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Unit test for NoCommand")
public class NoCommandTest {

    private HelpUnknownService helpUnknownService;
    private NoCommand noCommand;

    @BeforeEach
    public void init() {
        helpUnknownService = Mockito.mock(HelpUnknownService.class);
        noCommand = new NoCommand(helpUnknownService);
    }

    @Test
    public void shouldProperlyExecuteCommand() throws TelegramApiException {
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        String chatId = "12345";

        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(12345L);

        noCommand.execute(update);

        verify(helpUnknownService).sendNoText(chatId);
        verifyNoMoreInteractions(helpUnknownService);
    }

    @Test
    public void shouldReturnCorrectCommandName() {
        assertEquals("NO", noCommand.getCommandName());
    }
}
