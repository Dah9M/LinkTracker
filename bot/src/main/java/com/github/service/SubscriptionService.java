package com.github.service;

import com.github.UrlValidator;
import com.github.exception.SubscriptionException;
import com.github.exception.UrlValidationException;
import com.github.repository.SubscriptionRepository;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static com.github.service.MessageName.*;

@Service
public class SubscriptionService implements SubscriptionServiceInterface {
    private final SendMessageInterface sendMessageService;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               SendMessageInterface sendMessageService) {
        this.subscriptionRepository = subscriptionRepository;
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void addSubscription(String chatId, String[] message) throws TelegramApiException {
        try {
            String url = UrlValidator.getUrlOrThrow(message);

            subscriptionRepository.addSubscription(chatId, url);

            sendMessageService.sendMessage(chatId, TRACK_MESSAGE.getMessageName());

        } catch (UrlValidationException e) {
            switch (e.getErrorType()) {
                case NO_URL -> sendMessageService.sendMessage(chatId, NO_URL_MESSAGE.getMessageName());
                case INVALID_URL -> sendMessageService.sendMessage(chatId, NO_VALID_URL.getMessageName());
            }
        } catch (SubscriptionException e) {
            switch (e.getErrorType()) {
                case DUPLICATE ->
                        sendMessageService.sendMessage(chatId, DUPLICATE_MESSAGE.getMessageName());

                case NO_URL ->
                        sendMessageService.sendMessage(chatId, NO_VALID_URL.getMessageName());

                case NO_SUBS ->
                        sendMessageService.sendMessage(chatId, NO_SUBS_MESSAGE.getMessageName());
            }
        }
    }

    @Override
    public void deleteSubscription(String chatId, String[] message) throws TelegramApiException {
        try {
            String url = UrlValidator.getUrlOrThrow(message);

            subscriptionRepository.deleteSubscription(chatId, url);

            sendMessageService.sendMessage(chatId, UNTRACK_MESSAGE.getMessageName());

        } catch (UrlValidationException e) {
            switch (e.getErrorType()) {
                case NO_URL -> sendMessageService.sendMessage(chatId, NO_URL_MESSAGE.getMessageName());
                case INVALID_URL -> sendMessageService.sendMessage(chatId, NO_VALID_URL.getMessageName());
            }

        } catch (SubscriptionException e) {
            switch (e.getErrorType()) {
                case NO_SUBS -> sendMessageService.sendMessage(chatId, NO_SUBS_MESSAGE.getMessageName());

                case NO_URL -> sendMessageService.sendMessage(chatId, NO_SUCH_URL.getMessageName());
            }
        }
    }

    @Override
    public void getSubscriptions(String chatId) throws TelegramApiException {
        List<String> userSubs = subscriptionRepository.getSubscriptionsById(chatId);
        if (userSubs == null || userSubs.isEmpty()) {
            sendMessageService.sendMessage(chatId, NO_SUBS_MESSAGE.getMessageName());
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < userSubs.size(); index++) {
            sb.append(index + 1).append(". ").append(userSubs.get(index)).append("\n");
        }

        sendMessageService.sendMessage(chatId, sb.toString());
    }
}
