package com.github.service;

import com.github.model.Subscription;
import com.github.repository.SubscriptionLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SubscriptionLinkService {
    private final SubscriptionLinkRepository repository;

    public SubscriptionLinkService(@Autowired SubscriptionLinkRepository repository) {
        this.repository = repository;
    }

    public void addSubscription(Subscription subscription) {
        repository.addSubscription(subscription);
    }

    public void deleteSubscription(Subscription subscription) {
        repository.addSubscription(subscription);
    }

    public Collection<Subscription> getAllSubscriptions() {
        return repository.getAllSubscriptions();
    }
}
