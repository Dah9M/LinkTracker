package com.github.repository;

import com.github.exception.SubscriptionErrorType;
import com.github.exception.SubscriptionException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SubscriptionRepository {
    private final Map<String, List<String>> subscriptionMap = new HashMap<>();

    public void addSubscriptionToDatabase(String chatId, String url) {
        List<String> userSubs = subscriptionMap.get(chatId);

        if (userSubs == null) {
            userSubs = new ArrayList<>();
            subscriptionMap.put(chatId, userSubs);
        }

        if (userSubs.contains(url)) {
            throw new SubscriptionException(
                    SubscriptionErrorType.DUPLICATE,
                    "Пользователь уже подписан на данный ресурс"
            );
        }
        userSubs.add(url);
    }

    public void deleteSubscriptionFromDatabase(String chatId, String url) {
        List<String> userSubs = subscriptionMap.get(chatId);

        if (userSubs == null) {
            throw new SubscriptionException(
                    SubscriptionErrorType.NO_SUBS,
                    "У пользователя нет подписок"
            );
        }

        boolean removed = userSubs.remove(url);
        if (!removed) {
            throw new SubscriptionException(
                    SubscriptionErrorType.NO_URL,
                    "Нет такой подписки у пользователя"
            );
        }
    }

    public List<String> getSubscriptionsById(String chatId) {
        return subscriptionMap.get(chatId);
    }
}
