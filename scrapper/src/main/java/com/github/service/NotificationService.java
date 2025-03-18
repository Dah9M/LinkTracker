package com.github.service;

import com.github.controller.BotController;
import com.github.model.LinkUpdate;
import com.github.model.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Value("${bot.update.url}")
    private String botUpdateUrl;

    private final BotController botController;
    private final RestTemplate restTemplate = new RestTemplate();

    public NotificationService(@Autowired BotController botController) {
        this.botController = botController;
    }

    public void sendNotification(LinkUpdate linkUpdate) {
        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(botUpdateUrl, linkUpdate, Void.class);
            // TODO: обработать четырехсотую
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Уведомление успешно!");
            }
        } catch (Exception e) {
            System.out.println("Исключение");
        }

    }
}
