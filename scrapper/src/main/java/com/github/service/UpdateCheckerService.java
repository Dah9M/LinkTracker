package com.github.service;

import com.github.client.GithubClient;
import com.github.client.StackOverflowCLient;
import com.github.model.LinkUpdate;
import com.github.model.ResourceType;
import com.github.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UpdateCheckerService {
    private final SubscriptionLinkService subscriptionLinkService;
    private final GithubClient githubClient;
    private final StackOverflowCLient stackOverflowCLient;
    private final NotificationService notificationService;

    @Autowired
    public UpdateCheckerService(SubscriptionLinkService subscriptionLinkService,
                                GithubClient githubClient,
                                StackOverflowCLient stackOverflowCLient,
                                NotificationService notificationService) {
        this.subscriptionLinkService = subscriptionLinkService;
        this.githubClient = githubClient;
        this.stackOverflowCLient = stackOverflowCLient;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedDelay = 60000)
    public void checkUpdates() {
        for (Subscription subscription : subscriptionLinkService.getAllSubscriptions()) {
            Optional<Instant> newUpdateOpt = Optional.empty();

            if (subscription.getType() == ResourceType.GITHUB) {
                newUpdateOpt = githubClient.fetchGithubUpdates(subscription.getUrl());
            } else if (subscription.getType() == ResourceType.STACKOVERFLOW) {
                newUpdateOpt = stackOverflowCLient.fetchStackOverflowUpdates(subscription.getUrl());
            }

            if (newUpdateOpt.isPresent()) {
                Instant newUpdate = newUpdateOpt.get();

                if (subscription.getLastUpdated() == null || newUpdate.isAfter(subscription.getLastUpdated())) {
                    LinkUpdate linkUpdate = new LinkUpdate(1L, subscription.getUrl(), "descr", List.of(subscription.getChatId()));
                    notificationService.sendNotification(linkUpdate);

                    subscription.setLastUpdated(newUpdate);
                }
            }
        }
    }
}
