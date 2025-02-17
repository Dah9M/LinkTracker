package com.github.bot;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.mockito.Mockito.*;

public class LinkTrackerBotTests {

    @Test
    void testOnUpdateReceived() throws Exception {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("/start");
        when(message.getChatId()).thenReturn(123456789L);

        LinkTrackerBot bot = spy(new LinkTrackerBot());
        doReturn("TestBot").when(bot).getBotUsername();
        doReturn("123456:TEST").when(bot).getBotToken();

        doReturn(null).when(bot).execute(any(SendMessage.class));

        bot.onUpdateReceived(update);

        verify(bot, times(1)).execute(any(SendMessage.class));
    }

}
