package com.github.repository;

import com.github.model.Subscription;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class SubscriptionLinkRepository {
    private final Map<String, Subscription> map = new HashMap<>();

    private String generateKey(String chatId, String url) {
        return chatId + url;
    }

    public void addSubscription(Subscription subscription) {
        map.put(generateKey(subscription.getChatId(), subscription.getUrl()), subscription);
    }

    public void removeSubscription(Subscription subscription) {
        map.remove(generateKey(subscription.getChatId(), subscription.getUrl()));
    }

    public Collection<Subscription> getAllSubscriptions() {
        return map.values();
    }
}
