package service;

import model.SubscriptionNotification;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@NoArgsConstructor
@Slf4j
@Service
public class NotificationService {

    @Value("${bot.update.url}")
    private String botUpdateUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public void sendNotification(SubscriptionNotification notification) {
        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(botUpdateUrl, notification, Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Уведомление успешно отправлено: {}", notification);
            } else {
                log.warn("Ошибка при отправке уведомления. Статус: {}. Ответ: {}", response.getStatusCode(), response);
                // TODO: Реализовать обработку статусов 4xx (например, повторные попытки или уведомление об ошибке)
            }
        } catch (Exception e) {
            log.error("Ошибка при отправке уведомления: {}", e.getMessage(), e);
        }
    }
}
