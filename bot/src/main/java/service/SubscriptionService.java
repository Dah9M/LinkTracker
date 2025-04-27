package service;

import core.UrlValidator;
import exception.SubscriptionErrorType;
import exception.SubscriptionException;
import exception.UrlValidationException;
import lombok.extern.slf4j.Slf4j;
import model.ResourceType;
import model.entity.AppUser;
import model.entity.Link;
import model.entity.Subscription;
import repository.AppUserRepository;
import repository.LinkRepository;
import repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static service.MessageName.*;

@Slf4j
@Service
public class SubscriptionService implements SubscriptionServiceInterface {

    private final SendMessageInterface sendMessageService;
    private final SubscriptionRepository subscriptionRepository;
    private final AppUserRepository appUserRepository;
    private final LinkRepository linkRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               SendMessageInterface sendMessageService,
                               AppUserRepository appUserRepository,
                               LinkRepository linkRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.sendMessageService = sendMessageService;
        this.appUserRepository = appUserRepository;
        this.linkRepository = linkRepository;
    }

    @Override
    public void addSubscription(String chatId, String[] message) throws TelegramApiException {
        log.info("Запрос на добавление подписки для chatId = {}", chatId);
        try {
            String url = UrlValidator.getUrlOrThrow(message);
            ResourceType type = UrlValidator.detectSource(url);

            Optional<Subscription> existingSub = subscriptionRepository.findByUser_ChatIdAndLink_Url(chatId, url);
            if (existingSub.isPresent()) {
                log.warn("Попытка добавить уже существующую подписку для chatId = {}, url = {}", chatId, url);
                throw new SubscriptionException(SubscriptionErrorType.DUPLICATE, DUPLICATE_MESSAGE.getMessageName());
            }
            AppUser user = appUserRepository.findByChatId(chatId).
                    orElseGet(() -> appUserRepository.save(new AppUser(chatId)));

            Link link = linkRepository.findByUrl(url).
                    orElseGet(() -> linkRepository.save(new Link(url, type)));

            Subscription newSubscription = new Subscription(user, link);
            subscriptionRepository.save(newSubscription);
            sendMessageService.sendMessage(chatId, TRACK_MESSAGE.getMessageName());
            log.info("Подписка успешно добавлена для chatId = {}, url = {}", chatId, url);

        } catch (UrlValidationException e) {
            log.warn("Ошибка валидации URL для chatId = {}: {}", chatId, e.getMessage());
            switch (e.getErrorType()) {
                case NO_URL -> sendMessageService.sendMessage(chatId, NO_URL_MESSAGE.getMessageName());
                case INVALID_URL -> sendMessageService.sendMessage(chatId, NO_VALID_URL.getMessageName());
            }
        } catch (SubscriptionException e) {
            log.warn("Ошибка подписки для chatId = {}: {}", chatId, e.getMessage());
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
        log.info("Запрос на удаление подписки для chatId = {}", chatId);
        try {
            String url = UrlValidator.getUrlOrThrow(message);
            Optional<Subscription> subscriptionOpt = subscriptionRepository.findByUser_ChatIdAndLink_Url(chatId, url);
            if (subscriptionOpt.isPresent()) {
                subscriptionRepository.delete(subscriptionOpt.get());
                sendMessageService.sendMessage(chatId, UNTRACK_MESSAGE.getMessageName());
                log.info("Подписка успешно удалена для chatId = {}, url = {}", chatId, url);
            } else {
                sendMessageService.sendMessage(chatId, NO_SUCH_URL.getMessageName());
                log.info("Подписка на url = {} для chatId = {} не найдена.", url, chatId);
            }
        } catch (UrlValidationException e) {
            log.warn("Ошибка валидации URL при удалении подписки для chatId = {}: {}", chatId, e.getMessage());
            switch (e.getErrorType()) {
                case NO_URL -> sendMessageService.sendMessage(chatId, NO_URL_MESSAGE.getMessageName());
                case INVALID_URL -> sendMessageService.sendMessage(chatId, NO_VALID_URL.getMessageName());
            }
        } catch (SubscriptionException e) {
            log.warn("Ошибка подписки при удалении для chatId = {}: {}", chatId, e.getMessage());
            switch (e.getErrorType()) {
                case NO_SUBS ->
                        sendMessageService.sendMessage(chatId, NO_SUBS_MESSAGE.getMessageName());
                case NO_URL ->
                        sendMessageService.sendMessage(chatId, NO_SUCH_URL.getMessageName());
            }
        }
    }

    @Override
    public void getSubscriptions(String chatId) throws TelegramApiException {
        log.info("Запрос списка подписок для chatId = {}", chatId);
        List<Subscription> subscriptions = subscriptionRepository.findByUser_ChatId(chatId);
        if (subscriptions == null || subscriptions.isEmpty()) {
            sendMessageService.sendMessage(chatId, NO_SUBS_MESSAGE.getMessageName());
            log.info("У пользователя с chatId = {} нет активных подписок.", chatId);
            return;
        }

        List<String> urls = subscriptions.stream()
                .map(sub -> sub.getLink().getUrl())
                .toList();
        sendMessageService.sendSubscriptions(chatId, urls);
        log.info("Список подписок отправлен пользователю с chatId = {} ({} подписок).", chatId, urls.size());
    }
}
