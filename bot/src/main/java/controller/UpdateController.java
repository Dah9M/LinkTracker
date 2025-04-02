package controller;

import model.SubscriptionNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.NotificationService;

@RestController
@RequestMapping("/")
public class UpdateController {

    private final NotificationService notificationService;

    @Autowired
    public UpdateController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/notifications")
    public ResponseEntity<Void> receiveNotification(@RequestBody SubscriptionNotification notification) {
        if (notification.getUsers() == null || notification.getTitle() == null) {
            return ResponseEntity.badRequest().build();
        }

        notificationService.sendNotification(notification);
        return ResponseEntity.ok().build();
    }
}
