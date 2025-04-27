package service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.SubscriptionNotification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    @Value("${bot.update.url}")
    private String botUpdateUrl;
    private final RetryTemplate retryTemplate;
    private final RestTemplate restTemplate = new RestTemplate();

    public void sendNotification(SubscriptionNotification notification) {
        retryTemplate.execute(context -> {
            try {
                log.debug("Отправка уведомления на URL: {}", botUpdateUrl);

                ResponseEntity<Void> response = restTemplate.postForEntity(botUpdateUrl, notification, Void.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("Уведомление успешно отправлено: {}", notification);
                } else {
                    log.warn("Ошибка при отправке уведомления. Статус: {}. Ответ: {}", response.getStatusCode(), response);

                    if (response.getStatusCode().is4xxClientError() && response.getStatusCode().value() != 429) {
                        // ошибка клиента не 429 — нет смысла ретраить, выбрасываем исключение
                        throw new IllegalStateException("Ошибка клиента при отправке уведомления: " + response.getStatusCode());
                    }
                    // иначе, либо 429, либо 5xx — даем ретраить
                    throw new RuntimeException("Ошибка отправки уведомления. Статус: " + response.getStatusCode());
                }
            } catch (Exception e) {
                log.error("Ошибка при отправке уведомления: {}", e.getMessage(), e);
                throw e;
            }
            return null;
        });
    }
}