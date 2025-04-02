package service;

import model.SubscriptionNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class NotificationService {

    private final SendMessageInterface sendMessageService;

    @Autowired
    public NotificationService(SendMessageInterface sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    public void sendNotification(SubscriptionNotification notification) {
        String message = "Обнаружено обновление по ссылке: " + notification.getTitle();

        for (String chatId : notification.getUsers()) {
            try {
                sendMessageService.sendMessage(chatId, message);
            } catch (TelegramApiException e) {
                System.out.println("Логи");
            }
        }
    }
}
