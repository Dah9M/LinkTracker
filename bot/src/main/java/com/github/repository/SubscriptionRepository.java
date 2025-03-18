package com.github.repository;

import com.github.entity.ChatUserEntity;
import com.github.entity.LinkEntity;
import com.github.entity.SubscriptionEntity;
import com.github.entity.SubscriptionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SubscriptionRepository {

    private final SubscriptionSpringDataRepository subscriptionJpa;
    private final ChatUserSpringDataRepository chatUserJpa;
    private final LinkSpringDataRepository linkJpa;

    @Autowired
    public SubscriptionRepository(SubscriptionSpringDataRepository subscriptionJpa,
                                  ChatUserSpringDataRepository chatUserJpa,
                                  LinkSpringDataRepository linkJpa) {
        this.subscriptionJpa = subscriptionJpa;
        this.chatUserJpa = chatUserJpa;
        this.linkJpa = linkJpa;
    }

    public void addSubscription(String chatId, String url) {
        ChatUserEntity user = chatUserJpa.findByChatId(chatId);
        if (user == null) {
            throw new RuntimeException("Пользователь с chatId=" + chatId + " не найден");
        }

        LinkEntity link = linkJpa.findByUrl(url);
        if (link == null) {
            throw new RuntimeException("Ссылка " + url + " не найдена в таблице link");
        }

        SubscriptionId subId = new SubscriptionId(user.getId(), link.getId());
        if (subscriptionJpa.existsById(subId)) {
            throw new RuntimeException("Пользователь уже подписан на данный ресурс");
        }

        SubscriptionEntity entity = new SubscriptionEntity();
        entity.setId(subId);
        entity.setUser(user);
        entity.setLink(link);
        entity.setCreatedAt(LocalDateTime.now());

        subscriptionJpa.save(entity);
    }

    public void deleteSubscription(String chatId, String url) {
        ChatUserEntity user = chatUserJpa.findByChatId(chatId);
        if (user == null) {
            throw new RuntimeException("Пользователь не найден");
        }

        LinkEntity link = linkJpa.findByUrl(url);
        if (link == null) {
            throw new RuntimeException("Ссылка не найдена");
        }

        SubscriptionId subId = new SubscriptionId(user.getId(), link.getId());
        if (!subscriptionJpa.existsById(subId)) {
            throw new RuntimeException("Нет такой подписки у пользователя");
        }

        subscriptionJpa.deleteById(subId);
    }

    public List<String> getSubscriptionsById(String chatId) {
        ChatUserEntity user = chatUserJpa.findByChatId(chatId);
        if (user == null) {
            return List.of();
        }

        List<SubscriptionEntity> all = subscriptionJpa.findAll();
        return all.stream()
                .filter(s -> s.getUser().getId().equals(user.getId()))
                .map(s -> s.getLink().getUrl())
                .collect(Collectors.toList());
    }
}

