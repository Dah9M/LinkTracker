package com.github.command;

import com.github.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Unit test for StartCommand")
public class StartCommandTest {

    private UserService userService;
    private StartCommand startCommand;

    @BeforeEach
    public void init() {
        userService = Mockito.mock(UserService.class);
        startCommand = new StartCommand(userService);
    }

    @Test
    public void shouldProperlyExecuteCommand() throws TelegramApiException {
        // given
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        String chatId = "12345";

        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(12345L);

        startCommand.execute(update);

        verify(userService).addUser(chatId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldReturnCorrectCommandName() {
        assertEquals("/start", startCommand.getCommandName());
    }
}
