package com.github.service;

import com.github.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static com.github.service.MessageName.*;
import static org.mockito.Mockito.*;

@DisplayName("Unit test for SubscriptionService")
public class SubscriptionServiceTest {

    private SubscriptionRepository subscriptionRepository;
    private SendMessageInterface sendMessageService;
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        subscriptionRepository = Mockito.mock(SubscriptionRepository.class);
        sendMessageService = Mockito.mock(SendMessageInterface.class);

        subscriptionService = new SubscriptionService(subscriptionRepository, sendMessageService);
    }

    @Test
    void shouldSendNoUrlMessageIfNoUrlProvided() throws TelegramApiException {
        String chatId = "123";
        String[] message = {"/track"};

        subscriptionService.addSubscription(chatId, message);

        verify(sendMessageService).sendMessage(chatId, NO_URL_MESSAGE.getMessageName());
        verifyNoInteractions(subscriptionRepository);
    }

    @Test
    void shouldSendNoValidUrlIfUrlIsInvalid() throws TelegramApiException {
        String chatId = "123";
        String[] message = {"/track", "htp://bad"};
        subscriptionService.addSubscription(chatId, message);

        verify(sendMessageService).sendMessage(chatId, NO_VALID_URL.getMessageName());
        verifyNoInteractions(subscriptionRepository);
    }

    @Test
    void shouldAddSubscriptionSuccessfully() throws TelegramApiException {
        String chatId = "123";
        String[] message = {"/track", "https://github.com"};
        when(subscriptionRepository.addSubscriptionToDatabase(chatId, "https://github.com"))
                .thenReturn("OK");

        subscriptionService.addSubscription(chatId, message);

        verify(subscriptionRepository).addSubscriptionToDatabase(chatId, "https://github.com");
        verify(sendMessageService).sendMessage(chatId, TRACK_MESSAGE.getMessageName());
    }

    @Test
    void shouldSendDuplicateMessageIfAlreadyExists() throws TelegramApiException {
        String chatId = "123";
        String[] message = {"/track", "https://github.com"};
        when(subscriptionRepository.addSubscriptionToDatabase(chatId, "https://github.com"))
                .thenReturn("DUPLICATE");

        subscriptionService.addSubscription(chatId, message);

        verify(sendMessageService).sendMessage(chatId, DUPLICATE_MESSAGE.getMessageName());
    }

    @Test
    void shouldSendNoSubsMessageIfUserNotFoundOnDelete() throws TelegramApiException {
        String chatId = "123";
        String[] message = {"/untrack", "https://github.com"};
        when(subscriptionRepository.deleteSubscriptionFromDatabase(chatId, "https://github.com"))
                .thenReturn("NO_SUBS");

        subscriptionService.deleteSubscription(chatId, message);

        verify(sendMessageService).sendMessage(chatId, NO_SUBS_MESSAGE.getMessageName());
    }

    @Test
    void shouldDeleteSubscriptionOk() throws TelegramApiException {
        String chatId = "123";
        String[] message = {"/untrack", "https://github.com"};
        when(subscriptionRepository.deleteSubscriptionFromDatabase(chatId, "https://github.com"))
                .thenReturn("OK");

        subscriptionService.deleteSubscription(chatId, message);

        verify(sendMessageService).sendMessage(chatId, UNTRACK_MESSAGE.getMessageName());
    }

    @Test
    void shouldSendNoSubsMessageIfNoSubscriptionsOnList() throws TelegramApiException {
        String chatId = "123";
        // вернём null, как если пользователь не в map’е
        when(subscriptionRepository.getSubscriptionsById(chatId)).thenReturn(null);

        subscriptionService.getSubscriptions(chatId);

        verify(sendMessageService).sendMessage(chatId, NO_SUBS_MESSAGE.getMessageName());
    }

    @Test
    void shouldSendSubscriptionsList() throws TelegramApiException {
        String chatId = "123";
        when(subscriptionRepository.getSubscriptionsById(chatId))
                .thenReturn(List.of("https://github.com", "https://stackoverflow.com"));

        subscriptionService.getSubscriptions(chatId);

        verify(sendMessageService).sendMessage(eq(chatId), contains("1. https://github.com\n2. https://stackoverflow.com"));
    }
}
