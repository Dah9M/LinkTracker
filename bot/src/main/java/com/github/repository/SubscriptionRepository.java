package com.github.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SubscriptionRepository {
    private final Map<String, List<String>> subscriptionMap = new HashMap<>();

    public List<String> getSubscriptionsById(String chatId) {
        return subscriptionMap.get(chatId);
    }

    public String addSubscriptionToDatabase(String chatId, String url) {
        List<String> userSubs = subscriptionMap.get(chatId);

        if (userSubs != null) {
            if (userSubs.contains(url)) {
                return "DUPLICATE";
            }

            userSubs.add(url);
            subscriptionMap.put(chatId, userSubs);
            return "OK";
        }

        List<String> newUser = new ArrayList<>();
        newUser.add(url);
        subscriptionMap.put(chatId, newUser);
        return "OK";
    }

    public String deleteSubscriptionFromDatabase(String chatId, String url) {
        List<String> userSubs = subscriptionMap.get(chatId);

        if (userSubs != null) {
            if (userSubs.contains(url)) {
                userSubs.remove(url);
                return "OK";
            } else {
                return "NO_URL";
            }
        } else {
            return "NO_SUBS";
        }
    }
}
