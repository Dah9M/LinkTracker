package service;

import lombok.extern.slf4j.Slf4j;
import model.SubscriptionNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class NotificationService {

    private final SendMessageInterface sendMessageService;

    @Autowired
    public NotificationService(SendMessageInterface sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    public void sendNotification(SubscriptionNotification notification) {
        String message = "Обнаружено обновление по ссылке: " + notification.getTitle();
        log.info("Начинается отправка уведомления о ссылке '{}' для {} пользователей.",
                notification.getTitle(), notification.getUsers().size());

        for (String chatId : notification.getUsers()) {
            try {
                sendMessageService.sendMessage(chatId, message);
                log.info("Уведомление успешно отправлено пользователю с chatId = {}", chatId);
            } catch (TelegramApiException e) {
                log.error("Ошибка при отправке уведомления пользователю с chatId = {}: {}", chatId, e.getMessage(), e);
            }
        }
    }
}
