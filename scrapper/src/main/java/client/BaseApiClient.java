package client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Slf4j
public abstract class BaseApiClient {

    protected final RestTemplate restTemplate = new RestTemplate();

    protected Optional<ResponseEntity<Map>> fetchApiResponse(String apiUrl, String originalUrl) {
        try {
            log.info("Отправка запроса по адресу API: {}", apiUrl);

            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.debug("Успешный ответ от API для исходного URL: {}", originalUrl);
                return Optional.of(response);
            } else {
                log.warn("API вернул неуспешный статус {} для исходного URL {}", response.getStatusCode(), originalUrl);
            }
        } catch (HttpClientErrorException.TooManyRequests ex) {
            log.error("Превышен лимит запросов к API для URL {}: {}", originalUrl, ex.getMessage(), ex);
        } catch (Exception e) {
            log.error("Ошибка при запросе к API для URL {}: {}", originalUrl, e.getMessage(), e);
        }
        return Optional.empty();
    }
}