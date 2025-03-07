package com.github.service;

import com.github.repository.SubscriptionRepository;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static com.github.UrlValidator.isValidUrl;
import static com.github.service.MessageName.*;

@Service
public class SubscriptionService implements SubscriptionServiceInterface {
    private final SendMessageInterface sendMessageService;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository, SendMessageInterface sendMessageService) {
        this.subscriptionRepository = subscriptionRepository;
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void addSubscription(String chatId, String[] message) throws TelegramApiException {
        String urlStatus = isValidUrl(message);
        String url;

        switch (urlStatus) {
            case "NO_URL":
                sendMessageService.sendMessage(chatId, NO_URL_MESSAGE.getMessageName());
                return;
            case "NO_VALID":
                sendMessageService.sendMessage(chatId, NO_VALID_URL.getMessageName());
                return;
            default:
                url = message[1];
        }

        String addStatus = subscriptionRepository.addSubscriptionToDatabase(chatId, url);

        if (addStatus.equals("OK")) {
            sendMessageService.sendMessage(chatId, TRACK_MESSAGE.getMessageName());
        } else {
            sendMessageService.sendMessage(chatId, DUPLICATE_MESSAGE.getMessageName());
        }
    }

    @Override
    public void deleteSubscription(String chatId, String[] message) throws TelegramApiException {
        String urlStatus = isValidUrl(message);
        String url;

        switch (urlStatus) {
            case "NO_URL":
                sendMessageService.sendMessage(chatId, NO_URL_MESSAGE.getMessageName());
                return;
            case "NO_VALID":
                sendMessageService.sendMessage(chatId, NO_VALID_URL.getMessageName());
                return;
            default:
                url = message[1];
        }

        String deleteStatus = subscriptionRepository.deleteSubscriptionFromDatabase(chatId, url);

        switch (deleteStatus) {
            case "NO_SUBS":
                sendMessageService.sendMessage(chatId, NO_SUBS_MESSAGE.getMessageName());
                return;
            case "NO_URL":
                sendMessageService.sendMessage(chatId, NO_SUCH_URL.getMessageName());
                return;
            default:
                sendMessageService.sendMessage(chatId, UNTRACK_MESSAGE.getMessageName());
        }
    }

    @Override
    public void getSubscriptions(String chatId) throws TelegramApiException {
        List<String> userSubs = subscriptionRepository.getSubscriptionsById(chatId);
        StringBuilder stringBuilder = new StringBuilder();

        if (userSubs == null) {
            sendMessageService.sendMessage(chatId, NO_SUBS_MESSAGE.getMessageName());
            return;
        }

        stringBuilder.append("1. ");
        stringBuilder.append(userSubs.getFirst());
        stringBuilder.append("\n");

        for (int index = 1; index < userSubs.size(); index++) {
            stringBuilder.append(index + 1);
            stringBuilder.append(". ");
            stringBuilder.append(userSubs.get(index));
            stringBuilder.append("\n");
        }

        sendMessageService.sendMessage(chatId, stringBuilder.toString());
    }
}
