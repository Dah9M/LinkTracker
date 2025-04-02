package service;

import core.UrlValidator;
import exception.SubscriptionErrorType;
import exception.SubscriptionException;
import exception.UrlValidationException;
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
        try {
            String url = UrlValidator.getUrlOrThrow(message);
            ResourceType type = UrlValidator.detectSource(url);

            Optional<Subscription> existingSub = subscriptionRepository.findByUser_ChatIdAndLink_Url(chatId, url);
            if (existingSub.isPresent()) {
                throw new SubscriptionException(SubscriptionErrorType.DUPLICATE, DUPLICATE_MESSAGE.getMessageName());
            }
            AppUser user = appUserRepository.findByChatId(chatId).
                    orElseGet(() -> appUserRepository.save(new AppUser(chatId)));

            Link link = linkRepository.findByUrl(url).
                    orElseGet(() -> linkRepository.save(new Link(url, type)));

            Subscription newSubscription = new Subscription(user, link);
            subscriptionRepository.save(newSubscription);
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
            Optional<Subscription> subscriptionOpt = subscriptionRepository.findByUser_ChatIdAndLink_Url(chatId, url);
            if (subscriptionOpt.isPresent()) {
                subscriptionRepository.delete(subscriptionOpt.get());
                sendMessageService.sendMessage(chatId, UNTRACK_MESSAGE.getMessageName());
            } else {
                sendMessageService.sendMessage(chatId, NO_SUCH_URL.getMessageName());
            }
        } catch (UrlValidationException e) {
            switch (e.getErrorType()) {
                case NO_URL -> sendMessageService.sendMessage(chatId, NO_URL_MESSAGE.getMessageName());
                case INVALID_URL -> sendMessageService.sendMessage(chatId, NO_VALID_URL.getMessageName());
            }
        } catch (SubscriptionException e) {
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
        List<Subscription> subscriptions = subscriptionRepository.findByUser_ChatId(chatId);
        if (subscriptions == null || subscriptions.isEmpty()) {
            sendMessageService.sendMessage(chatId, NO_SUBS_MESSAGE.getMessageName());
            return;
        }
        String message = subscriptions.stream()
                .map(sub -> sub.getLink().getUrl())
                .collect(Collectors.joining("\n"));
        sendMessageService.sendMessage(chatId, message);
    }
}
