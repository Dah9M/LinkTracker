package service;

import client.GithubClient;
import client.StackOverflowCLient;
import lombok.extern.slf4j.Slf4j;
import model.entity.Link;
import model.ResourceType;
import model.entity.Subscription;
import model.SubscriptionNotification;
import repository.LinkRepository;
import repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UpdateCheckerService {

    private final SubscriptionRepository subscriptionRepository;
    private final LinkRepository linkRepository;
    private final GithubClient githubClient;
    private final StackOverflowCLient stackOverflowClient;
    private final NotificationService notificationService;

    @Autowired
    public UpdateCheckerService(LinkRepository linkRepository,
                                SubscriptionRepository subscriptionRepository,
                                GithubClient githubClient,
                                StackOverflowCLient stackOverflowClient,
                                NotificationService notificationService) {
        this.linkRepository = linkRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.githubClient = githubClient;
        this.stackOverflowClient = stackOverflowClient;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedDelay = 60000)
    public void checkUpdates() {
        log.info("Запуск проверки обновлений...");

        List<Subscription> subscriptions = subscriptionRepository.findAll();

        Map<Link, List<Subscription>> subscriptionsByLink = subscriptions.stream()
                .collect(Collectors.groupingBy(Subscription::getLink));

        for (Map.Entry<Link, List<Subscription>> entry : subscriptionsByLink.entrySet()) {
            Link link = entry.getKey();
            List<Subscription> subs = entry.getValue();

            try {
                Optional<Instant> newUpdateOpt = fetchLastUpdate(link);

                if (newUpdateOpt.isPresent()) {
                    Instant newUpdate = newUpdateOpt.get();
                    if (link.getUpdatedAt() == null || newUpdate.isAfter(link.getUpdatedAt())) {
                        log.info("Обнаружено обновление для ссылки {}. Новое обновление: {}", link.getUrl(), newUpdate);
                        link.setUpdatedAt(newUpdate);
                        linkRepository.save(link);

                        List<String> chatIds = subs.stream()
                                .map(sub -> sub.getUser().getChatId())
                                .collect(Collectors.toList());

                        SubscriptionNotification notification = new SubscriptionNotification(link.getUrl(), newUpdate, chatIds);
                        notificationService.sendNotification(notification);

                        log.info("Отправлены уведомления пользователям: {}", chatIds);
                    } else {
                        log.info("Обновлений для ссылки {} не обнаружено.", link.getUrl());
                    }
                } else {
                    log.info("Не удалось получить информацию об обновлении для ссылки {}.", link.getUrl());
                }
            } catch (Exception e) {
                log.error("Ошибка при проверке обновлений для ссылки {}: {}", link.getUrl(), e.getMessage(), e);
            }
        }

        log.info("Проверка обновлений завершена.");
    }

    private Optional<Instant> fetchLastUpdate(Link link) {
        if (link.getResourceType() == ResourceType.GITHUB) {
            Optional<Instant> pushUpdate = githubClient.fetchGithubUpdates(link.getUrl());
            Optional<Instant> issuesUpdate = githubClient.fetchLatestIssueOrPrUpdate(link.getUrl());

            if (pushUpdate.isPresent() && issuesUpdate.isPresent()) {
                Instant chosenUpdate = pushUpdate.get().isAfter(issuesUpdate.get())
                        ? pushUpdate.get()
                        : issuesUpdate.get();
                return Optional.of(chosenUpdate);
            } else if (pushUpdate.isPresent()) {
                return pushUpdate;
            } else {
                return issuesUpdate;
            }
        } else if (link.getResourceType() == ResourceType.STACKOVERFLOW) {
            return stackOverflowClient.fetchStackOverflowUpdates(link.getUrl());
        } else {
            log.warn("Ресурс с неизвестным типом: {}.", link.getResourceType());
            return Optional.empty();
        }
    }
}
