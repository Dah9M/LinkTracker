package controller;

import lombok.extern.slf4j.Slf4j;
import model.SubscriptionNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.NotificationService;

@RestController
@RequestMapping("/")
@Slf4j
public class UpdateController {

    private final NotificationService notificationService;

    @Autowired
    public UpdateController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/notifications")
    public ResponseEntity<Void> receiveNotification(@RequestBody SubscriptionNotification notification) {
        log.info("Получен запрос на отправку уведомления: {}", notification);
        if (notification.getUsers() == null || notification.getTitle() == null) {
            log.warn("Неверный запрос: отсутствуют пользователи или заголовок.");
            return ResponseEntity.badRequest().build();
        }

        notificationService.sendNotification(notification);
        log.info("Уведомление успешно отправлено.");
        return ResponseEntity.ok().build();
    }
}
